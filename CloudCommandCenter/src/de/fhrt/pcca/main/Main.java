package de.fhrt.pcca.main;

import java.io.IOException;
import java.util.Set;

import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.compute.domain.Template;
import org.jclouds.openstack.nova.v2_0.domain.KeyPair;

public class Main {

	public static void main(String[] args) throws IOException {
		CloudFabric fabric = new CloudFabric();
		try {
			KeyPair keypair = fabric.createKeyPair();
			Template template = fabric.createSimpleTemplate(keypair);
			Set<? extends NodeMetadata> nodes = fabric.createServer(template, 5);
			fabric.doSSHCommand(nodes.iterator().next(), "sudo mkdir hello");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			fabric.close();
		}
	}

}
