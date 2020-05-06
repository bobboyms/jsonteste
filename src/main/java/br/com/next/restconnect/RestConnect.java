package br.com.next.restconnect;

import br.com.next.restconnect.interfaces.RestaData;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;

import javax.net.ssl.SSLContext;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

public final class RestConnect implements RestaData<String> {

    private static final String USER_NAME = "rodriguesthiago@brq.com";
    private static final String PASSWORD = "rodrigues";

    private static final String BASE_URL = "https://bitbucket.prebanco.com.br/";
    private static final String ENDPOINT_PULL_REQUESTS = "rest/api/1.0/projects/NEXT/repos/nextbank/pull-requests?state=MERGED";
    private static final String ENDPOINT_PULL_REQUEST_DIFF = "rest/api/1.0/projects/NEXT/repos/nextbank/pull-requests/%s/diff";
    private static final String ENDPOINT_PULL_REQUEST_COMMITS = "rest/api/1.0/projects/NEXT/repos/nextbank/pull-requests/%s/commits";

    private RestConnect() {
        ignoreExpiredSSL();
    }

    public static RestaData<String> build() {
        return new RestConnect();
    }

    private void ignoreExpiredSSL() {

        SSLContext sslContext = null;

        try {
            sslContext = new SSLContextBuilder().loadTrustMaterial(null, new TrustSelfSignedStrategy() {
                @Override
                public boolean isTrusted(X509Certificate[] chain, String authType) {
                    return true;
                }
            }).build();

            CloseableHttpClient customHttpClient = HttpClients.custom().setSSLContext(sslContext)
                    .setSSLHostnameVerifier(new NoopHostnameVerifier()).build();
            Unirest.setHttpClient(customHttpClient);

        } catch (NoSuchAlgorithmException | KeyManagementException | KeyStoreException e) {
            e.printStackTrace();
        }

    }

    @Override
    public HttpResponse<String> getPullRequestsCommits(String idPullRequest) throws UnirestException {
        return Unirest.get(BASE_URL + String.format(ENDPOINT_PULL_REQUEST_COMMITS, idPullRequest))
                .basicAuth(USER_NAME,PASSWORD)
                .asString();
    }

    @Override
    public HttpResponse<String> getPullRequestsDiff(String idPullRequest) throws UnirestException {
        return Unirest.get(BASE_URL + String.format(ENDPOINT_PULL_REQUEST_DIFF, idPullRequest))
                .basicAuth(USER_NAME,PASSWORD)
                .asString();
    }

    @Override
    public HttpResponse<String> getPullRequests() throws UnirestException {
        return Unirest.get(BASE_URL + ENDPOINT_PULL_REQUESTS)
                .basicAuth(USER_NAME,PASSWORD)
                .asString();
    }
}
