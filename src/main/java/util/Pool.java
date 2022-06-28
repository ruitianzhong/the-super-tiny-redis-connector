package util;

import exceptions.RedisException;
import org.apache.commons.pool2.PooledObjectFactory;
import org.apache.commons.pool2.impl.GenericObjectPool;

public class Pool <T> extends GenericObjectPool<T> {

    public Pool(PooledObjectFactory<T> factory) {
        super(factory);
    }
    public void returnNormalResource( T resource){
        if(resource==null){
            return;
        }
        try{
            returnObject(resource);
        }catch (Exception e){
            throw new RedisException(e);
        }
    }
    public void returnBrokenConnection(T resource){
        if(resource==null){
            return;
        }
        try{
            invalidateObject(resource);
        } catch (Exception e) {
            throw new RedisException(e);
        }
    }
    public T getResource(){
        try {
           return super.borrowObject();
        }catch (Exception ex){
            throw new RedisException("Can not get the resource");
        }
    }
}
