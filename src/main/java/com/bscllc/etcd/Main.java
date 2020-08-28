package com.bscllc.etcd;


import io.etcd.jetcd.ByteSequence;
import io.etcd.jetcd.Client;
import io.etcd.jetcd.KV;
import io.etcd.jetcd.KeyValue;
import io.etcd.jetcd.kv.GetResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.Charset;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class Main {
    private static final Logger LOGGER = LoggerFactory.getLogger(Main.class);
    private static Charset UTF8 = Charset.forName("UTF-8");

    public static void main(String... args) {
        try {
            Client client = Client.builder().endpoints("http://localhost:2379").build();
            KV kvClient = client.getKVClient();

            ByteSequence key = ByteSequence.from("Common/Log".getBytes());

// get the CompletableFuture
            CompletableFuture<GetResponse> getFuture = kvClient.get(key);

// get the value from CompletableFuture
            GetResponse response = getFuture.get();

            LOGGER.info(String.format("Response count: %d\n", response.getCount()));

            List<KeyValue> list = response.getKvs();

            list.forEach(item -> {
                ByteSequence bkey = item.getKey();
                ByteSequence bval = item.getValue();

                String str = String.format("\tKey: %s. Value: %s", bkey.toString(UTF8), bval.toString(UTF8));

                LOGGER.info(str);
            });



            client.close();
        }
        catch(Exception exp) {
            exp.printStackTrace();
        }
    }
}
