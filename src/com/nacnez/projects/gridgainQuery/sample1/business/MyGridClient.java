package com.nacnez.projects.gridgainQuery.sample1.business;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Map.Entry;
import java.util.UUID;

import org.gridgain.client.GridClient;
import org.gridgain.client.GridClientConfigurationAdapter;
import org.gridgain.client.GridClientData;
import org.gridgain.client.GridClientDataConfigurationAdapter;
import org.gridgain.client.GridClientException;
import org.gridgain.client.GridClientFactory;
import org.gridgain.client.GridClientPartitionedAffinity;
import org.gridgain.client.GridClientPredicate;
import org.gridgain.grid.GridConfigurationAdapter;
import org.gridgain.grid.typedef.X;

import com.nacnez.projects.gridgainQuery.sample1.model.Person;
//import org.gridgain.client.Gr

public class MyGridClient {

	/** Grid node address to connect to. */
	private static final String SERVER_ADDRESS = "127.0.0.1";

	public static void main(String[] args) throws Exception {
		clientCacheExample();
	}

	private static void clientCacheExample() throws GridClientException {
		GridClient client = createClient();
		// Random node ID.
		X.println(">>> Client created, current grid topology: "
				+ client.compute().nodes());

		final UUID randNodeId = client.compute().nodes().iterator().next()
				.nodeId();
		X.println(randNodeId.toString());
		// Get client projection of grid partitioned cache.
		GridClientData rmtCache = client.data("partitioned");
		
		Collection<Person> data = new GridFiller().createData(10);
		Collection<String> keys = new ArrayList<String>();
		// for (int i = 0;i<10;i++) {
		for (Person p : data) {
			String key = p.getUniqueId();
			// String key = Integer.toString(i);
			UUID nodeId = rmtCache.affinity(key);
			X.println(nodeId.toString());
			try {

			rmtCache.put(key, p);
			
			} catch (GridClientException e) {
				System.out.println("Client Exception");
				e.printStackTrace();
				throw e;
			} catch (Exception e) {
				System.out.println("SomeThing else");
				e.printStackTrace();
			}
			
			// rmtCache.put(key, "value of: " + i);
			X.println(">>> Storing key " + key + " on node " + nodeId);
			keys.add(key);
		}
		GridClientData prj = rmtCache.pinNodes(client.compute()
				.node(randNodeId));

		// Request batch from our local node in pinned mode.
		Map<String, Object> vals = prj.getAll(keys);

		for (Map.Entry<String, Object> entry : vals.entrySet())
			X.println(">>> Loaded cache entry [key=" + entry.getKey()
					+ ", val=" + entry.getValue() + ']');

	}

	private static GridClient createClient() throws GridClientException {
		GridClientConfigurationAdapter cfg = new GridClientConfigurationAdapter();

		GridClientDataConfigurationAdapter cacheCfg = new GridClientDataConfigurationAdapter();

		// Set remote cache name.
		cacheCfg.setName("partitioned");
		// Set client partitioned affinity for this cache.
		cacheCfg.setAffinity(new GridClientPartitionedAffinity());

		cfg.setDataConfigurations(Collections.singletonList(cacheCfg));

		// Point client to a local node. Note that this server is only used
		// for initial connection. After having established initial connection
		// client will make decisions which grid node to use based on
		// collocation
		// with key affinity or load balancing.
		cfg.setServers(Collections.singletonList(SERVER_ADDRESS + ':'
				+ GridConfigurationAdapter.DFLT_TCP_PORT));

		return GridClientFactory.start(cfg);
	}

}
