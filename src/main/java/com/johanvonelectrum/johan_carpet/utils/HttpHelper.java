package com.johanvonelectrum.johan_carpet.utils;

import com.johanvonelectrum.johan_carpet.JohanExtension;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

public class HttpHelper {

    private static final String LATEST_RELEASE = "GET https://api.github.com/repos/JohanVonElectrum/Johan-Carpet/releases/latest";

    public static String getLatestRelease() {
        String response = null;
        try {
            response = sendRequest(LATEST_RELEASE);
            int i = response.indexOf("tag_name");
            if (i > -1) {
                 response = response.substring(i + 11);
                 response = response.substring(0, response.indexOf("\""));
            }
        } catch (IOException exception) {
            exception.printStackTrace();
            response = JohanExtension.VERSION;
        }
        return response;
    }

    public static String sendRequest(String uri) throws IOException { //METHOD uri/?params
        String[] uri_components = uri.split(" ", 2);
        String method = uri_components[0].toUpperCase();
        uri_components = uri_components[1].split("\\?", 2);
        String uri_main = uri_components[0];

        URL url = new URL(uri_main);
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod(method);

        if (uri.contains("?")) {
            Map<String, String> parameters = new HashMap<>();
            for (String input: uri_components[1].split("&")) {
                String[] temp = input.split("=", 2);
                if (input.contains("="))
                    parameters.put(temp[0], temp[1]);
                else
                    parameters.put(temp[0], "true");
            }

            con.setDoOutput(true);
            DataOutputStream out = new DataOutputStream(con.getOutputStream());
            out.writeBytes(ParameterStringBuilder.getParamsString(parameters));
            out.flush();
            out.close();
        }

        int status = con.getResponseCode();

        Reader streamReader = null;

        if (status > 299) {
            streamReader = new InputStreamReader(con.getErrorStream());
        } else {
            streamReader = new InputStreamReader(con.getInputStream());
        }

        BufferedReader in = new BufferedReader(streamReader);
        String inputLine;
        StringBuffer content = new StringBuffer();
        while ((inputLine = in.readLine()) != null) {
            content.append(inputLine);
        }
        in.close();

        con.disconnect();

        return content.toString();
    }

    static class ParameterStringBuilder {
        public static String getParamsString(Map<String, String> params) throws UnsupportedEncodingException {
            StringBuilder result = new StringBuilder();

            for (Map.Entry<String, String> entry : params.entrySet()) {
                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
                result.append("&");
            }

            String resultString = result.toString();
            return resultString.length() > 0
                    ? resultString.substring(0, resultString.length() - 1)
                    : resultString;
        }
    }
}
