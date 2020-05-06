package br.com.next;

import br.com.next.factorydatamodel.FactoryDataModel;
import br.com.next.restconnect.RestConnect;

import java.util.List;

public class Main {

    public static void main(String[] args) throws Exception {
        List<DataModel> dataModels = FactoryDataModel.create(RestConnect.build());
        dataModels.forEach(System.out::println);
    }

}
