package com.example.helloworld;

import java.util.concurrent.TimeUnit;

import javax.jmdns.ServiceInfo;

import com.example.helloworld.GreeterGrpc.GreeterBlockingStub;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.StatusRuntimeException;

public class HelloWorldClient {
	public static void main(String[] args) throws Exception {
		String host = "localhost";
		int port = 50051;
		
		ServiceInfo serviceInfo1 = JmDNSDiscovery.run("_grpc._tcp.local.");
		ServiceInfo serviceInfo2 = JmDNSDiscovery.run("_http._tcp.local.");
		ServiceInfo serviceInfo3 = JmDNSDiscovery.run("_hello._tcp.local.");
		ServiceInfo serviceInfo4 = JmDNSDiscovery.run("_bye._tcp.local.");
		
		System.out.println("service running on port: " + serviceInfo1.getPort());
		System.out.println("service running on port: " + serviceInfo2.getPort());
		System.out.println("service running on port: " + serviceInfo3.getPort());
		System.out.println("service running on port: " + serviceInfo4.getPort());
		
		ManagedChannel channel1 = ManagedChannelBuilder
				.forAddress(host, serviceInfo1.getPort())
				.usePlaintext()
				.build();
		ManagedChannel channel2 = ManagedChannelBuilder
				.forAddress(host, serviceInfo2.getPort())
				.usePlaintext()
				.build();
		ManagedChannel channel3 = ManagedChannelBuilder
				.forAddress(host, serviceInfo3.getPort())
				.usePlaintext()
				.build();
		ManagedChannel channel4 = ManagedChannelBuilder
				.forAddress(host, serviceInfo4.getPort())
				.usePlaintext()
				.build();
		GreeterBlockingStub stub = GreeterGrpc.newBlockingStub(channel1);
		
		try {
			HelloRequest request = HelloRequest.newBuilder().setName("World").build();
			HelloReply reply = stub.sayHello(request);
			
			System.out.println(reply.getMessage());
		} catch(StatusRuntimeException e) {
			e.printStackTrace();
		} finally {
			channel1.shutdown().awaitTermination(60,TimeUnit.SECONDS);
		}
	}
}
