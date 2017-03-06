package com.axolotl.kohdneyms;

import java.io.IOException;

import io.grpc.Server;
import io.grpc.ServerBuilder;

public class KohdneymsServer {
	public static void main (String [] args) throws IOException, InterruptedException {
		Server server = ServerBuilder.forPort(9090).addService(new KohdneymsService()).build();
		server.start();
		server.awaitTermination();
	}
}
