package client;

import com.google.gson.Gson;
import service.requests.RegisterRequest;
import service.results.RegisterResult;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;

public class ServerFacade {

    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public void register(String username, String password, String email) throws ResponseException {
        var path = "/user";
        var registerRequest = new RegisterRequest(username, password, email);
        this.makeRequest("POST", path, registerRequest, RegisterResult.class);
    }

    public void login() throws ResponseException {
        var path = "/session";
        this.makeRequest("POST", path, null, null);
    }

    public void logout() throws ResponseException {
        var path = "/session";
        this.makeRequest("DELETE", path, null, null);
    }

    public void listGames() throws ResponseException {
        var path = "/game";
        this.makeRequest("GET", path, null, null);
    }

    public void createGame() throws ResponseException {
        var path = "/game";
        this.makeRequest("POST", path, null, null);
    }

    public void joinGame() throws ResponseException {
        var path = "/game";
        this.makeRequest("PUT", path, null, null);
    }

    public void clear() throws ResponseException {
        var path = "/db";
        this.makeRequest("DELETE", path, null, null);
    }

//    public Pet addPet(Pet pet) throws ResponseException {
//        var path = "/pet";
//        return this.makeRequest("POST", path, pet, Pet.class);
//    }
//
//    public void deletePet(int id) throws ResponseException {
//        var path = String.format("/pet/%s", id);
//        this.makeRequest("DELETE", path, null, null);
//    }
//
//    public void deleteAllPets() throws ResponseException {
//        var path = "/pet";
//        this.makeRequest("DELETE", path, null, null);
//    }
//
//    public Pet[] listPets() throws ResponseException {
//        var path = "/pet";
//        record listPetResponse(Pet[] pet) {
//        }
//        var response = this.makeRequest("GET", path, null, listPetResponse.class);
//        return response.pet();
//    }

    private <T> T makeRequest(String method, String path, Object request, Class<T> responseClass) throws ResponseException {
        try {
            URL url = (new URI(serverUrl + path)).toURL();
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod(method);
            http.setDoOutput(true);

            writeBody(request, http);
            http.connect();
            throwIfNotSuccessful(http);
            return readBody(http, responseClass);
        } catch (ResponseException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new ResponseException(500, ex.getMessage());
        }
    }

    private static void writeBody(Object request, HttpURLConnection http) throws IOException {
        if (request != null) {
            http.addRequestProperty("Content-Type", "application/json");
            String reqData = new Gson().toJson(request);
            try (OutputStream reqBody = http.getOutputStream()) {
                reqBody.write(reqData.getBytes());
            }
        }
    }

    private void throwIfNotSuccessful(HttpURLConnection http) throws IOException, ResponseException {
        var status = http.getResponseCode();
        if (!isSuccessful(status)) {
            try (InputStream respErr = http.getErrorStream()) {
                if (respErr != null) {
                    throw ResponseException.fromJson(status, respErr);
                }
            }

            throw new ResponseException(500, "other failure: " + status);
        }
    }

    private static <T> T readBody(HttpURLConnection http, Class<T> responseClass) throws IOException {
        T response = null;
        if (http.getContentLength() < 0) {
            try (InputStream respBody = http.getInputStream()) {
                InputStreamReader reader = new InputStreamReader(respBody);
                if (responseClass != null) {
                    response = new Gson().fromJson(reader, responseClass);
                }
            }
        }
        return response;
    }

    private boolean isSuccessful(int status) {
        return status / 100 == 2;
    }
}
