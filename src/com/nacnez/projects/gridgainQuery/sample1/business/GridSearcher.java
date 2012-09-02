package com.nacnez.projects.gridgainQuery.sample1.business;

import static org.gridgain.grid.cache.query.GridCacheQueryType.SQL;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.gridgain.grid.Grid;
import org.gridgain.grid.GridProjection;
import org.gridgain.grid.cache.GridCacheProjection;
import org.gridgain.grid.cache.affinity.GridCacheAffinityKey;
import org.gridgain.grid.cache.query.GridCacheQuery;
import org.gridgain.grid.cache.query.GridCacheQueryFuture;
import org.gridgain.grid.cache.query.GridCacheReduceQuery;
import org.gridgain.grid.lang.GridReducer;
import org.gridgain.grid.lang.GridTuple;
import org.gridgain.grid.typedef.C1;
import org.gridgain.grid.typedef.F;

import com.nacnez.projects.gridgainQuery.sample1.model.Person;

public class GridSearcher {

	/** Cache name. */
	private static final String CACHE_NAME = "partitioned";
	// private static final String CACHE_NAME = "replicated";

	GridProjection p;
	GridCacheProjection<GridCacheAffinityKey<String>, Person> cache;

	// public void searchStuff(Grid grid) throws Exception {
	// initializeCache(grid);
	// incomeQueries(cache,p);
	// }

	private void initializeCache(Grid grid) {
		p = grid;
		cache = cache(grid);
	}

	// void incomeQueries(GridCacheProjection<GridCacheAffinityKey<String>,
	// Person> cache, GridProjection p) throws Exception {
	// GridCacheQuery<GridCacheAffinityKey<String>, Person> qry =
	// cache.createQuery(SQL, Person.class,"income>=?");
	// Collection<?> output = qry.queryArguments(new
	// BigDecimal("5000.00")).execute(p).get();
	// System.out.println(output.size());
	// }

	public void getAllPersons(Grid grid) throws Exception {
		initializeCache(grid);
		GridCacheQuery<GridCacheAffinityKey<String>, Person> qry = cache
				.createQuery(SQL, Person.class, "income>=?");
		Collection<?> output = qry.queryArguments(new BigDecimal("5000.00"))
				.execute(p).get();
	}

	public void getHigherHalfSalariedPersons(Grid grid) throws Exception {
		initializeCache(grid);
		GridCacheQuery<GridCacheAffinityKey<String>, Person> qry = cache
				.createQuery(SQL, Person.class, "income>=?");
		Collection<?> output = qry.queryArguments(new BigDecimal("3000000.00"))
				.execute(p).get();
	}

	public void getFirstTenHigherHalfSalariedPersons(Grid grid) throws Exception {
		initializeCache(grid);
		GridCacheQuery<GridCacheAffinityKey<String>, Person> qry = cache
				.createQuery(SQL, Person.class, "income>=?");
		//qry.pageSize(10);
		qry.keepAll(false);
		
		GridCacheQueryFuture<Entry<GridCacheAffinityKey<String>, Person>> qfuture  = qry.queryArguments(new BigDecimal("3000000.00")).execute(p);
		int count = 0;
		List<Person> output = new ArrayList<Person>();
		while (qfuture.hasNext() && count<10) {
			output.add(qfuture.next().getValue());
			count++;
		}
//		Collection<?> output = qry.queryArguments(new BigDecimal("3000000.00")).execute(p).get();
		System.out.println(output.size());
	}

	
	public void getCountOfHigherHalfSalariedPersons(Grid grid) throws Exception {
		initializeCache(grid);
		GridCacheReduceQuery<GridCacheAffinityKey<String>, Person, GridTuple<Integer>, Integer> qry = cache
				.createReduceQuery(SQL, Person.class, "income>=?");

		qry.remoteReducer(new C1<Object[], GridReducer<Map.Entry<GridCacheAffinityKey<String>, Person>, GridTuple<Integer>>>() {
			private GridReducer<Map.Entry<GridCacheAffinityKey<String>, Person>, GridTuple<Integer>> rdc = new GridReducer<Map.Entry<GridCacheAffinityKey<String>, Person>, GridTuple<Integer>>() {
				private int cnt;

				@Override
				public boolean collect(
						Map.Entry<GridCacheAffinityKey<String>, Person> e) {
					cnt++;
					return true;
				}

				@Override
				public GridTuple<Integer> apply() {
					return F.t(cnt);
				}
			};

			@Override
			public GridReducer<Map.Entry<GridCacheAffinityKey<String>, Person>, GridTuple<Integer>> apply(
					Object[] args) {
				return rdc;
			}
		});
		
        qry.localReducer(new C1<Object[], GridReducer<GridTuple<Integer>, Integer>>() {
            private GridReducer<GridTuple<Integer>, Integer> rdc =
                new GridReducer<GridTuple<Integer>, Integer>() {
                    private int cnt;

                    @Override public boolean collect(GridTuple<Integer> e) {
                        cnt += e.get();

                        // Continue collecting.
                        return true;
                    }

                    @Override public Integer apply() {
                        return cnt;
                    }
                };

                @Override public GridReducer<GridTuple<Integer>, Integer> apply(Object[] args) {
                    return rdc;
                }
            });

		// Collection<?> output = qry.queryArguments(new
		// BigDecimal("3000000.00")).execute(p).get();

        Integer count = qry.queryArguments(new BigDecimal("3000000.00")).reduce(p).get();
        System.out.println(count);

	}

	public void getPersonsFromBangalore(Grid grid) throws Exception {
		initializeCache(grid);
		GridCacheQuery<GridCacheAffinityKey<String>, Person> qry = cache
				.createQuery(SQL, Person.class, "city=?");
		Collection<?> output = qry.queryArguments("Bangalore")
				.execute(p).get();
		System.out.println(output.size());
	}

	public void getCountOfPersonsFromBangalore(Grid grid) throws Exception {
		initializeCache(grid);
		GridCacheReduceQuery<GridCacheAffinityKey<String>, Person, GridTuple<Integer>, Integer> qry = cache
				.createReduceQuery(SQL, Person.class, "city=?");

		qry.remoteReducer(new C1<Object[], GridReducer<Map.Entry<GridCacheAffinityKey<String>, Person>, GridTuple<Integer>>>() {
			private GridReducer<Map.Entry<GridCacheAffinityKey<String>, Person>, GridTuple<Integer>> rdc = new GridReducer<Map.Entry<GridCacheAffinityKey<String>, Person>, GridTuple<Integer>>() {
				private int cnt;

				@Override
				public boolean collect(
						Map.Entry<GridCacheAffinityKey<String>, Person> e) {
					cnt++;
					return true;
				}

				@Override
				public GridTuple<Integer> apply() {
					return F.t(cnt);
				}
			};

			@Override
			public GridReducer<Map.Entry<GridCacheAffinityKey<String>, Person>, GridTuple<Integer>> apply(
					Object[] args) {
				return rdc;
			}
		});
		
        qry.localReducer(new C1<Object[], GridReducer<GridTuple<Integer>, Integer>>() {
            private GridReducer<GridTuple<Integer>, Integer> rdc =
                new GridReducer<GridTuple<Integer>, Integer>() {
                    private int cnt;

                    @Override public boolean collect(GridTuple<Integer> e) {
                        cnt += e.get();

                        // Continue collecting.
                        return true;
                    }

                    @Override public Integer apply() {
                        return cnt;
                    }
                };

                @Override public GridReducer<GridTuple<Integer>, Integer> apply(Object[] args) {
                    return rdc;
                }
            });

		// Collection<?> output = qry.queryArguments(new
		// BigDecimal("3000000.00")).execute(p).get();

        Integer count = qry.queryArguments("Bangalore").reduce(p).get();
        System.out.println(count);

	}

	private static <K, V> GridCacheProjection<K, V> cache(Grid grid) {
		return grid.cache(CACHE_NAME);
	}

}
