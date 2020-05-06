package br.com.next.restconnect.interfaces;

import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.exceptions.UnirestException;

public interface RestaData<T> {

    HttpResponse<T> getPullRequestsCommits(String idPullRequest) throws UnirestException;
    HttpResponse<T> getPullRequestsDiff(String idPullRequest) throws UnirestException;
    HttpResponse<T> getPullRequests() throws UnirestException;

}
