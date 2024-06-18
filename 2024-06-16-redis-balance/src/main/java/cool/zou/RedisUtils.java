package cool.zou;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;




public class RedisUtils {

    static JedisPool pool;

    static {
        JedisPoolConfig config = new JedisPoolConfig();
        config.setMaxIdle(100);
        config.setMaxTotal(200);
        config.setMinEvictableIdleTimeMillis(3000);
        config.setTimeBetweenEvictionRunsMillis(1000);
        config.setTestOnBorrow(false);
        config.setTestOnReturn(false);
        config.setTestWhileIdle(false);
        config.setTestOnCreate(false);
        pool = new JedisPool(config, "100.100.100.100", 6379);
    }

    public static void main(String[] args) {


        for (int i = 0; i < 100; i++) {
            int concurrencyLevel = 3000;
            for (int j = 0; j < concurrencyLevel; j++) {
                Thread thread = new Thread(new Worker());
                thread.start();
            }
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        try {
            Thread.sleep(300000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        
    }

    static class Worker implements Runnable {
        @Override
        public void run() {
            try  {
                Jedis jedis = pool.getResource();
                jedis.set("key", "value");
                System.out.println(jedis.get("key"));
                jedis.close();
            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}