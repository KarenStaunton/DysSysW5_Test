package com.example.helloworld;

import com.example.helloworld.GreeterGrpc.GreeterImplBase;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;

public class HelloWorldServer extends GreeterImplBase {
	public static void main(String[] args) {
		int port = 50051;
		
		HelloWorldServer worldWorldServer = new HelloWorldServer();
		JmDNSRegistration reg = new JmDNSRegistration();
		reg.run("_grpc._tcp.local.", "HelloService1", 50001, "running hello service");
		reg.run("_http._tcp.local.", "HelloService2", 50002, "running hello service");
		reg.run("_hello._tcp.local.", "HelloService3", 50003, "running hello service");
		reg.run("_bye._tcp.local.", "HelloService4", 50004, "running hello service");
		
		try {
			Server server = ServerBuilder.forPort(port)
					.addService(worldWorldServer)
					.build()
					.start();
			server.awaitTermination();
		} catch(Exception e) {
			System.out.println(e);
		}
	}
	
	@Override
	public void sayHello(HelloRequest request, StreamObserver<HelloReply> responseObserver) {
		System.out.println("you called the server");
		
		HelloReply reply = HelloReply.newBuilder()
				.setMessage("Hello " + request.getName()).build();
		responseObserver.onNext(reply);
		responseObserver.onCompleted();
	}
}
