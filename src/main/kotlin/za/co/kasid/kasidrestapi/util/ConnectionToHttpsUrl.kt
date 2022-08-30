package za.co.kasid.kasidrestapi.util

import java.io.InputStreamReader
import java.io.Reader
import java.net.URL
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.*


/**
 * Fix for Exception in thread "main" javax.net.ssl.SSLHandshakeException:
 * sun.security.validator.ValidatorException: PKIX path building failed:
 * sun.security.provider.certpath.SunCertPathBuilderException: unable to find
 * valid certification path to requested target
 */
object ConnectToHttpsUrl {

    @Throws(Exception::class)
    @JvmStatic
    fun main() {
        /* Start of Fix */
        val trustAllCerts = arrayOf<TrustManager>(object : X509TrustManager {
            override fun getAcceptedIssuers(): Array<X509Certificate>? {
                return null
            }

            override fun checkClientTrusted(certs: Array<X509Certificate>, authType: String) {}
            override fun checkServerTrusted(certs: Array<X509Certificate>, authType: String) {}
        })
        val sc = SSLContext.getInstance("SSL")
        sc.init(null, trustAllCerts, SecureRandom())
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.socketFactory)

        // Create all-trusting host name verifier
        val allHostsValid = HostnameVerifier { hostname, session -> true }
        // Install the all-trusting host verifier
        HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid)
        /* End of the fix*/
        val url = URL("https://app.kasid.co.za")
        val con = url.openConnection()
        val reader: Reader = InputStreamReader(con.getInputStream())
        while (true) {
            val ch = reader.read()
            if (ch == -1) break
            print(ch.toChar())
        }
    }

}