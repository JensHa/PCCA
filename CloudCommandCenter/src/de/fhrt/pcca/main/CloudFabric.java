package de.fhrt.pcca.main;

import static com.google.common.base.Charsets.UTF_8;
import static de.fhrt.pcca.main.Constants.CREDENTIAL;
import static de.fhrt.pcca.main.Constants.ENDPOINT;
import static de.fhrt.pcca.main.Constants.IDENTITY;
import static de.fhrt.pcca.main.Constants.NAME;
import static de.fhrt.pcca.main.Constants.POLL_PERIOD_TWENTY_SECONDS;
import static de.fhrt.pcca.main.Constants.PROVIDER;
import static de.fhrt.pcca.main.Constants.REGION;
import static org.jclouds.compute.config.ComputeServiceProperties.POLL_INITIAL_PERIOD;
import static org.jclouds.compute.config.ComputeServiceProperties.POLL_MAX_PERIOD;

import java.io.File;
import java.io.IOException;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.TimeoutException;

import org.jclouds.ContextBuilder;
import org.jclouds.compute.ComputeService;
import org.jclouds.compute.ComputeServiceContext;
import org.jclouds.compute.RunNodesException;
import org.jclouds.compute.domain.ComputeMetadata;
import org.jclouds.compute.domain.Image;
import org.jclouds.compute.domain.NodeMetadata;
import org.jclouds.compute.domain.Template;
import org.jclouds.openstack.nova.v2_0.NovaApi;
import org.jclouds.openstack.nova.v2_0.compute.options.NovaTemplateOptions;
import org.jclouds.openstack.nova.v2_0.domain.KeyPair;
import org.jclouds.openstack.nova.v2_0.domain.regionscoped.RegionAndId;
import org.jclouds.openstack.nova.v2_0.extensions.KeyPairApi;
import org.jclouds.ssh.SshClient;
import org.jclouds.sshj.config.SshjSshClientModule;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableSet;
import com.google.common.io.Closeables;
import com.google.common.io.Files;
import com.google.inject.Module;

public class CloudFabric {
	private final ComputeService computeService;
	private final NovaApi novaApi;
	private File keyPairFile;

	public CloudFabric() {

		String provider = PROVIDER;
		String identity = IDENTITY; // tenantName:userName
		String credential = CREDENTIAL;

		Iterable<Module> modules = ImmutableSet.<Module> of(new SshjSshClientModule());

		// These properties control how often jclouds polls for a status update
		Properties overrides = new Properties();
		overrides.setProperty(POLL_INITIAL_PERIOD, POLL_PERIOD_TWENTY_SECONDS);
		overrides.setProperty(POLL_MAX_PERIOD, POLL_PERIOD_TWENTY_SECONDS);

		ComputeServiceContext context = ContextBuilder.newBuilder(provider).endpoint(ENDPOINT)
				.credentials(identity, credential).overrides(overrides).modules(modules)
				.buildView(ComputeServiceContext.class);

		computeService = context.getComputeService();
		novaApi = context.unwrapApi(NovaApi.class);
	}

	/**
	 * print all installed keypairs
	 */
	public void listKeyPairs() {
		Optional<? extends KeyPairApi> keyPairApiExtension = novaApi.getKeyPairApi(REGION);

		if (keyPairApiExtension.isPresent()) {
			System.out.println("Key Pair Extension Present");

			KeyPairApi keyPairApi = keyPairApiExtension.get();

			for (KeyPair keyPair : keyPairApi.list()) {
				System.out.println(keyPair.getName());
			}
		}
	}

	/**
	 * Create a public key in the cloud and write the private key file to the
	 * local working directory.
	 */
	public KeyPair createKeyPair() throws IOException {
		System.out.format("  Create Key Pair%n");

		KeyPairApi keyPairApi = novaApi.getKeyPairApi(REGION).get();
		KeyPair keyPair = keyPairApi.create(NAME + "_" + ((int) (Math.random() * 10000)));

		keyPairFile = new File(keyPair.getName() + ".pem");

		Files.write(keyPair.getPrivateKey(), keyPairFile, UTF_8);

		System.out.println("Wrote " + keyPairFile.getAbsolutePath());

		return keyPair;
	}

	/**
	 * Create a server with the key pair.
	 * 
	 * @throws RunNodesException
	 */
	public Set<? extends NodeMetadata> createServer(Template template, int amount) throws RunNodesException {

		// This method will continue to poll for the server status and won't
		// return until this server is ACTIVE
		// If you want to know what's happening during the polling, enable
		// logging.
		// See
		// /jclouds-example/rackspace/src/main/java/org/jclouds/examples/rackspace/Logging.java
		Set<? extends NodeMetadata> nodes = computeService.createNodesInGroup(NAME, amount, template);
		return nodes;
	}

	/**
	 * Create a template with predefined options for a simple instance
	 * 
	 * @param keyPair
	 *            to use for the instance
	 * @return
	 */
	public Template createSimpleTemplate(KeyPair keyPair) {
		NovaTemplateOptions options = NovaTemplateOptions.Builder.keyPairName(keyPair.getName())
				.overrideLoginUser("centos").overrideLoginPrivateKey(keyPair.getPrivateKey());

		RegionAndId regionAndId = RegionAndId.fromRegionAndId(REGION, "2");
		Template template = computeService.templateBuilder().locationId(REGION).osDescriptionMatches(".*CentOS 7 RAW*")
				.hardwareId(regionAndId.slashEncode()).options(options).build();
		return template;
	}

	/**
	 * do a ssh-command on the specified node
	 * 
	 * @param node
	 *            for the ssh command
	 * @param command
	 *            to do
	 * @throws TimeoutException
	 */
	public void doSSHCommand(NodeMetadata node, String command) throws TimeoutException {

		SshClient client = computeService.getContext().utils().sshForNode().apply(node);
		client.connect();
		client.exec(command);

	}

	/**
	 * Always close your service when you're done with it.
	 */
	public void close() throws IOException {
		Closeables.close(computeService.getContext(), true);
	}

	/**
	 * Print all instances
	 */
	public void listAllInstances() {
		for (ComputeMetadata node : computeService.listNodes()) {
			System.out.println(node.toString());
		}
	}

	/**
	 * Print all images
	 */
	public void listAllImages() {
		for (Image image : computeService.listImages()) {
			System.out.println(image.toString());
		}
	}

}
