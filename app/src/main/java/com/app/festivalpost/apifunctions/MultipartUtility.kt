package com.app.festivalpost.apifunctions

import android.util.Log
import com.app.festivalpost.apifunctions.MultipartUtility
import kotlin.Throws
import com.app.festivalpost.apifunctions.NullHostNameVerifier
import com.app.festivalpost.apifunctions.NullX509TrustManager
import com.app.festivalpost.apifunctions.ApiEndpoints
import java.io.*
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLConnection
import java.security.KeyManagementException
import java.security.NoSuchAlgorithmException
import java.security.SecureRandom
import javax.net.ssl.HttpsURLConnection
import javax.net.ssl.SSLContext
import javax.net.ssl.X509TrustManager

/**
 * This utility class provides an abstraction layer for sending multipart HTTP
 * POST requests to a web server.
 *
 * @author www.codejava.net
 */
class MultipartUtility(requestURL: String?, private val charset: String) {
    private val boundary: String
    private var httpConn: HttpURLConnection? = null
    private var outputStream: OutputStream? = null
    private var writer: PrintWriter? = null

    /**
     * Adds a form field to the request
     *
     * @param name  field name
     * @param value field value
     */
    fun addFormField(name: String, value: String?) {
        writer!!.append("--$boundary").append(LINE_FEED)
        writer!!.append("Content-Disposition: form-data; name=\"$name\"")
            .append(LINE_FEED)
        writer!!.append("Content-Type: text/plain; charset=$charset").append(
            LINE_FEED
        )
        writer!!.append(LINE_FEED)
        writer!!.append(value).append(LINE_FEED)
        writer!!.flush()
    }

    /**
     * Adds a upload file section to the request
     *
     * @param fieldName  name attribute in <input type="file" name="..."></input>
     * @param uploadFile a File to be uploaded
     * @throws IOException
     */
    @Throws(IOException::class)
    fun addFilePart(fieldName: String, uploadFile: File) {
        val fileName = uploadFile.name
        writer!!.append("--$boundary").append(LINE_FEED)
        writer!!.append(
            "Content-Disposition: form-data; name=\"" + fieldName
                    + "\"; filename=\"" + fileName + "\""
        )
            .append(LINE_FEED)
        writer!!.append(
            "Content-Type: "
                    + URLConnection.guessContentTypeFromName(fileName)
        )
            .append(LINE_FEED)
        writer!!.append("Content-Transfer-Encoding: binary").append(LINE_FEED)
        writer!!.append(LINE_FEED)
        writer!!.flush()
        val inputStream = FileInputStream(uploadFile)
        val buffer = ByteArray(4096)
        var bytesRead = -1
        while (inputStream.read(buffer).also { bytesRead = it } != -1) {
            outputStream!!.write(buffer, 0, bytesRead)
        }
        outputStream!!.flush()
        inputStream.close()
        writer!!.append(LINE_FEED)
        writer!!.flush()
    }

    /**
     * Adds a header field to the request.
     *
     * @param name  - name of the header field
     * @param value - value of the header field
     */
    fun addHeaderField(name: String, value: String) {
        writer!!.append("$name: $value").append(LINE_FEED)
        writer!!.flush()
    }

    /**
     * Completes the request and receives response from the server.
     *
     * @return a list of Strings as response in case the server returned
     * status OK, otherwise an exception is thrown.
     * @throws IOException
     */
    @Throws(IOException::class)
    fun finish(): String {
        var response = String()
        writer!!.append(LINE_FEED).flush()
        writer!!.append("--$boundary--").append(LINE_FEED)
        writer!!.close()

        // checks server's status code first
        val status = httpConn!!.responseCode
        if (status == HttpURLConnection.HTTP_OK || status == 400 || status == 201) {
            val reader = BufferedReader(
                InputStreamReader(
                    httpConn!!.inputStream
                )
            )
            var line: String? = null
            while (reader.readLine().also { line = it } != null) {
                response = response + line
            }
            reader.close()
            httpConn!!.disconnect()
        } else {
            throw IOException("Server returned non-OK status: $status")
        }
        return response
    } //	public List<String> finish() throws IOException {

    //		List<String> response = new ArrayList<String>();
    //
    //		writer.append(LINE_FEED).flush();
    //		writer.append("--" + boundary + "--").append(LINE_FEED);
    //		writer.close();
    //
    //		// checks server's status code first
    //		int status = httpConn.getResponseCode();
    //		if (status == HttpURLConnection.HTTP_OK) {
    //			BufferedReader reader = new BufferedReader(new InputStreamReader(
    //					httpConn.getInputStream()));
    //			String line = null;
    //			while ((line = reader.readLine()) != null) {
    //				response.add(line);
    //			}
    //			reader.close();
    //			httpConn.disconnect();
    //		} else {
    //			throw new IOException("Server returned non-OK status: " + status);
    //		}
    //
    //		return response;
    //	}
    companion object {
        private const val LINE_FEED = "\r\n"
    }

    /**
     * This constructor initializes a new HTTP POST request with content type
     * is set to multipart/form-data
     *
     * @param requestURL
     * @param charset
     * @throws IOException
     */
    init {

        // creates a unique boundary based on time stamp
        boundary = "===" + System.currentTimeMillis() + "==="
        val sslcontext: SSLContext? = null
        try {
//			sslcontext = SSLContext.getInstance("TLSv1");
//			sslcontext.init(null,
//					null,
//					null);
//			SSLSocketFactory NoSSLv3Factory = new NoSSLv3SocketFactory(sslcontext.getSocketFactory());
//
//			HttpsURLConnection.setDefaultSSLSocketFactory(NoSSLv3Factory);
            HttpsURLConnection.setDefaultHostnameVerifier(NullHostNameVerifier())
            val context = SSLContext.getInstance("TLS")
            context.init(null, arrayOf<X509TrustManager>(NullX509TrustManager()), SecureRandom())
            HttpsURLConnection.setDefaultSSLSocketFactory(context.socketFactory)
            val url = URL(requestURL)
            httpConn = url.openConnection() as HttpURLConnection
            httpConn!!.useCaches = false
            httpConn!!.doOutput = true // indicates POST method
            httpConn!!.doInput = true
            httpConn!!.setRequestProperty(
                "Content-Type",
                "multipart/form-data; boundary=$boundary"
            )
            httpConn!!.setRequestProperty("User-Agent", "CodeJava Agent")
            httpConn!!.setRequestProperty(ApiEndpoints.API_KEY, ApiEndpoints.API_SECRET)
            outputStream = httpConn!!.outputStream
            writer = PrintWriter(
                OutputStreamWriter(outputStream, charset),
                true
            )
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
            Log.d("NoSuchErrror", "" + e.message)
        } catch (e: KeyManagementException) {
            e.printStackTrace()
            Log.d("NoSuchErrrorKeyManager", "" + e.message)
        }
    }
}