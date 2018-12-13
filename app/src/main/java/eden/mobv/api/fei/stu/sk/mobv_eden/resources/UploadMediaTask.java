package eden.mobv.api.fei.stu.sk.mobv_eden.resources;

import android.os.AsyncTask;
import com.crashlytics.android.Crashlytics;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Objects;

public class UploadMediaTask extends AsyncTask<String, Void, String> {

    private String pathToFile;

    @Override
    protected String doInBackground(String[] pathToOurFile) {
        pathToFile = pathToOurFile[0];
        HttpURLConnection connection;
        DataOutputStream outputStream;

        String urlServer = "http://mobv.mcomputing.eu/upload/index.php";
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary =  "*****";
        String serverResponseMessage;

        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1024*1024;

        try {
            File file = new File(pathToFile);
            FileInputStream fileInputStream = new FileInputStream(file);

            URL url = new URL(urlServer);
            connection = (HttpURLConnection) url.openConnection();

            // Allow Inputs & Outputs
            connection.setDoInput(true);
            connection.setDoOutput(true);
            connection.setUseCaches(false);

            // Enable POST method
            connection.setRequestMethod("POST");

            connection.setRequestProperty("Connection", "Keep-Alive");
            connection.setRequestProperty("Content-Type", "multipart/form-data;boundary="+boundary);

            String[] filePathSplitted = pathToFile.split("/");
            String filename = filePathSplitted[filePathSplitted.length - 1];

            outputStream = new DataOutputStream( connection.getOutputStream() );
            outputStream.writeBytes(twoHyphens + boundary + lineEnd);
            outputStream.writeBytes("Content-Disposition: form-data; name=\"" + "upfile" + "\";filename=\"" + filename + "\"" + lineEnd);
            outputStream.writeBytes(lineEnd);

            bytesAvailable = fileInputStream.available();
            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            // Read file
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {
                outputStream.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);
            }

            outputStream.writeBytes(lineEnd);
            outputStream.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            // JSON Response from the server (message)
            InputStream responseStream = new BufferedInputStream(connection.getInputStream());
            BufferedReader responseStreamReader = new BufferedReader(new InputStreamReader(responseStream));
            JSONObject jObject = new JSONObject(responseStreamReader.readLine());

            serverResponseMessage = jObject.getString("message");

            responseStreamReader.close();
            responseStream.close();
            connection.disconnect();
            fileInputStream.close();
            outputStream.flush();
            outputStream.close();

        }
        catch (Exception e) {
            Crashlytics.logException(e);
            //Exception handling
            serverResponseMessage = null;
        }
        return serverResponseMessage;
    }

    protected void onPostExecute(String response) {
        System.out.println(response);
        if (response != null) {
            String mediaUrl = "http://mobv.mcomputing.eu/upload/v/" + response;

            String userId;
            // exception handlig for current user's ID
            try {
                userId = Objects.requireNonNull(FirebaseAuth.getInstance().getCurrentUser()).getUid();
            }
            catch (Exception e) {
                Crashlytics.logException(e);
                userId = "jZt8S24UNOHhQmaIZXx5";
            }

            String username = "";
            // exception handlig for current user's username

            FirebaseAuth auth = FirebaseAuth.getInstance();
            if (auth != null) {
                FirebaseUser firebaseUser = auth.getCurrentUser();
                username = firebaseUser.getDisplayName();
            } else {
                Crashlytics.log("tu by sa mal dostat iba prihlaseny user");
            }

            FirestoreDatabase database = new FirestoreDatabase();
            database.addPost(isImageOrVideo(pathToFile), mediaUrl, userId, username);
        }
    }

    private String isImageOrVideo(String pathToFile) {
        String mimeType = URLConnection.guessContentTypeFromName(pathToFile);
        if (mimeType.startsWith("image"))
            return "image";
        return "video";
    }
}
