package com.enecuum.androidapp.websocketNew;


import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import com.appunite.websocket.rx.RxWebSockets;
import com.appunite.websocket.rx.object.RxObjectWebSockets;
import com.enecuum.androidapp.websocketNew.model.Message;
import com.enecuum.androidapp.websocketNew.model.MessageType;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import rx.schedulers.Schedulers;

public class PoANew {

    public PoANew() {
        final Gson gson = new GsonBuilder()
                .registerTypeAdapter(Message.class, new Message.Deserializer())
                .registerTypeAdapter(MessageType.class, new MessageType.SerializerDeserializer())
                .create();

        final OkHttpClient okHttpClient = new OkHttpClient();
        final RxWebSockets webSockets = new RxWebSockets(okHttpClient, new Request.Builder()
                .get()
                .url("ws://")
                .build());
        final GsonObjectSerializer serializer = new GsonObjectSerializer(gson, Message.class);
        final RxObjectWebSockets jsonWebSockets = new RxObjectWebSockets(webSockets, serializer);
        final SocketConnection socketConnection = new SocketConnectionImpl(jsonWebSockets, Schedulers.io());
    }
}
