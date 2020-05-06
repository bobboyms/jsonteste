package br.com.next.factorydatamodel;

import br.com.next.DataModel;
import br.com.next.restconnect.interfaces.RestaData;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashape.unirest.http.exceptions.UnirestException;

import java.io.IOException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class FactoryDataModel {

    private static final String VENDOR_BRQ = "brq";
    private static final String VENDOR_CAPCO = "cap";
    private static final String VENDOR_IBM = "ibm";
    private static final String VENDOR_FORSYSTEM = "fsy";

    private FactoryDataModel() {}

    private static RestaData<String> restaData;
    private static ObjectMapper mapper = new ObjectMapper();

    private static boolean isDevBranch(String displayId) {

        final String[] strSplited = displayId.split("/");

        if (Arrays.asList(strSplited).contains("dev")) {
            return true;
        }

        return false;
    }

    private static String getVendorName(String displayId) {

        final String[] strSplited = displayId.split("/");

        if (Arrays.asList(strSplited).contains(VENDOR_BRQ)) {
            return "BRQ";
        }
        if (Arrays.asList(strSplited).contains(VENDOR_CAPCO)) {
            return "CAPCO";
        }
        if (Arrays.asList(strSplited).contains(VENDOR_IBM)) {
            return "IBM";
        }
        if (Arrays.asList(strSplited).contains(VENDOR_FORSYSTEM)) {
            return "FOURSYSTEM";
        }

        return "NONE";

    }

    private static Long getQtdCommits(Long idPullRequest) {

        try {

            JsonNode root = mapper.readTree(FactoryDataModel.restaData.getPullRequestsCommits(idPullRequest.toString()).getRawBody());
            JsonNode values = root.get("values");

            if (values.isArray()) {
                return Long.valueOf(values.size());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0l;
    }

    private static Long getQtdFiles(Long idPullRequest)  {

        try {

            JsonNode root = mapper.readTree(FactoryDataModel.restaData.getPullRequestsDiff(idPullRequest.toString()).getRawBody());
            JsonNode values = root.get("diffs");

            if (values.isArray()) {
                return Long.valueOf(values.size());
            }

        } catch (Exception e) {

        }

        return 1l;
    }

    private static DataModel generateModel(JsonNode pollRequest) {

        final long id = pollRequest.get("id").asLong();

        Long qtdCommits = getQtdCommits(id);
        Long qtdFiles = getQtdFiles(id);
        JsonNode properties = pollRequest.get("properties");
        JsonNode fromRef = pollRequest.get("fromRef");
        String vendorName = getVendorName(fromRef.get("displayId").asText());
        String commentCount = properties.path("commentCount").asText();

        long count = 0l;
        if (commentCount.trim().length() > 0) {
            count = Long.valueOf(commentCount);
        }

        boolean dev = isDevBranch(fromRef.get("displayId").asText());
        long longDate = pollRequest.get("closedDate").asLong();

        return new DataModel(id, vendorName, count, new Date(longDate), dev, qtdCommits, qtdFiles);

    }

    public static List<DataModel> create(RestaData<String> restaData) throws UnirestException, IOException {

        FactoryDataModel.restaData = restaData;
        JsonNode root = mapper.readTree(restaData.getPullRequests().getRawBody());

        return StreamSupport.stream(root.get("values").spliterator(), true)
                .map(FactoryDataModel::generateModel)
                .filter(value -> value.isDev())
                .collect(Collectors.toList());
    }

}
