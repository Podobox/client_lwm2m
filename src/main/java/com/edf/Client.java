package com.edf;

import java.util.EnumSet;
import java.util.List;

import org.eclipse.leshan.client.LeshanClient;
import org.eclipse.leshan.client.LeshanClientBuilder;
import org.eclipse.leshan.client.object.Security;
import org.eclipse.leshan.client.object.Server;
import org.eclipse.leshan.client.resource.ObjectsInitializer;
import org.eclipse.leshan.core.LwM2mId;
import org.eclipse.leshan.core.endpoint.Protocol;
import org.eclipse.leshan.core.request.BindingMode;
import org.eclipse.leshan.transport.javacoap.client.endpoint.JavaCoapClientEndpointsProvider;

public class Client 
{
    public static void main( String[] args )
    {
        String endpoint = "RetDlwm2mdemo";
        LeshanClientBuilder builder = new LeshanClientBuilder(endpoint);

        ObjectsInitializer initializer = new ObjectsInitializer();
        BindingMode serverBindingMode = BindingMode.fromProtocol(Protocol.fromUri("coap://leshan.eclipseprojects.io:5683"));
        
        builder.setEndpointsProviders(List.of(new JavaCoapClientEndpointsProvider()));

        // Add objects
        initializer.setInstancesForObject(LwM2mId.SECURITY, Security.noSec("coap://leshan.eclipseprojects.io:5683", 12345));
        initializer.setInstancesForObject(LwM2mId.SERVER, new Server(12345, (5 * 60),
        EnumSet.of(serverBindingMode),false, BindingMode.U));
        initializer.setInstancesForObject(LwM2mId.DEVICE, new MyDevice());
        
        builder.setObjects(initializer.createAll());

        LeshanClient client = builder.build();
        client.start();
    }
}