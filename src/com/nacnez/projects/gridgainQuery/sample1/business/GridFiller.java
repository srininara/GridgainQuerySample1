package com.nacnez.projects.gridgainQuery.sample1.business;

import java.util.Collection;

import org.gridgain.grid.Grid;
import org.gridgain.grid.cache.GridCacheProjection;
import org.gridgain.grid.cache.affinity.GridCacheAffinityKey;
import org.gridgain.grid.typedef.G;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.nacnez.projects.gridgainQuery.sample1.model.Person;
import com.nacnez.projects.gridgainQuery.sample1.modelContract.PersonContract;
import com.nacnez.util.modelgen.ModelGenerator;
import com.nacnez.util.modelgen.impl.SimpleModelGenerator;
import com.nacnez.util.modelgen.impl.factory.ModelGenModule;

public class GridFiller {
    /** Cache name. */
    private static final String CACHE_NAME = "partitioned";
    // private static final String CACHE_NAME = "replicated";



	
	public void fillGridWithData(Grid grid, Collection<Person> data) throws Exception {
        GridCacheProjection<GridCacheAffinityKey<String>, Person> cache = cache(grid);
        GridCacheProjection<GridCacheAffinityKey<String>, Person> personCache =
                cache.projection(GridCacheAffinityKey.class, Person.class);
//        Collection<Person> data = createData();
        for (Person person: data) {
        	personCache.put(person.key(),person);
        }
		
	}
	
    private static <K, V> GridCacheProjection<K, V> cache(Grid grid) {
        return grid.cache(CACHE_NAME);
    }

	
	public Collection<Person> createData(int count) {
		Injector injector = Guice.createInjector(new ModelGenModule());

		ModelGenerator<Person> smg = new SimpleModelGenerator<Person>();
		injector.injectMembers(smg);
		Collection<Person> c = smg.make(count).instancesWith(PersonContract.class).andProvideAsCollection();
		return c;
	}
}
