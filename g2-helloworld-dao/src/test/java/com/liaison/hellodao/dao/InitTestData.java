package com.liaison.hellodao.dao;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.liaison.commons.jpa.DAOUtil;
import com.liaison.commons.jpa.Op;
import com.liaison.hellodao.model.HelloMoon;
import com.liaison.hellodao.model.HelloWorld;

public class InitTestData
{
	public static final String TEST_WORLD_SIID = "1234"; 
	
	public void initTestData() throws Exception
	{
		System.out.println( "Creating data..." );
        
        HelloWorld helloWorld = null;
    	HelloMoon helloMoon = null;
        
    	// Saturn and Moons
    	// -----------------
    	helloWorld = new HelloWorld();

    	helloWorld.setName( "Saturn" );
        helloWorld.setSiSguid( TEST_WORLD_SIID + "A" );
        helloWorld.setHelloMoons( new ArrayList<HelloMoon>() );
    	
        helloMoon = new HelloMoon();
        helloMoon.setName( "Mimas" );
        helloWorld.getHelloMoons().add( helloMoon );
        
        helloMoon = new HelloMoon();
        helloMoon.setName( "Tethys" );
        helloWorld.getHelloMoons().add( helloMoon );
        
        helloMoon = new HelloMoon();
        helloMoon.setName( "Dione" );
        helloWorld.getHelloMoons().add( helloMoon );
        
        helloMoon = new HelloMoon();
        helloMoon.setName( "Rhea" );
        helloWorld.getHelloMoons().add( helloMoon );
        
        helloMoon = new HelloMoon();
        helloMoon.setName( "Titan" );
        helloWorld.getHelloMoons().add( helloMoon );
        
        helloMoon = new HelloMoon();
        helloMoon.setName( "Iapetus" );
        helloWorld.getHelloMoons().add( helloMoon );
        
        DAOUtil.persist( helloWorld );
        
    	// Jupiter and Moons
    	// ------------------
        helloWorld = new HelloWorld();
        helloWorld.setName( "Jupiter" );
        helloWorld.setSiSguid( TEST_WORLD_SIID + "B" );
        helloWorld.setHelloMoons( new ArrayList<HelloMoon>() );
    	
        helloMoon = new HelloMoon();
        helloMoon.setName( "Metis" );
        helloWorld.getHelloMoons().add( helloMoon );

        helloMoon = new HelloMoon();
        helloMoon.setName( "Adrastea" );
        helloWorld.getHelloMoons().add( helloMoon );

        helloMoon = new HelloMoon();
        helloMoon.setName( "Amalthea" );
        helloWorld.getHelloMoons().add( helloMoon );
        
        helloMoon = new HelloMoon();
        helloMoon.setName( "Thebe" );
        helloWorld.getHelloMoons().add( helloMoon );

        helloMoon = new HelloMoon();
        helloMoon.setName( "Io" );
        helloWorld.getHelloMoons().add( helloMoon );
        
        DAOUtil.persist( helloWorld );
        
    	// Earth and Moon
    	// ---------------
        helloWorld = new HelloWorld();
        helloWorld.setName( "Earth" );
        helloWorld.setSiSguid( TEST_WORLD_SIID + "C" );
        helloWorld.setHelloMoons( new ArrayList<HelloMoon>() );
    	
        helloMoon = new HelloMoon();
        helloMoon.setName( "TheMoon" );
        helloWorld.getHelloMoons().add( helloMoon );
        
        DAOUtil.persist( helloWorld );
	}
	
	public void deleteTestData() throws Exception
	{
		System.out.println( "Deleting data..." );
		
    	Op op = new Op()
    	{
			@SuppressWarnings("unchecked")
			@Override
			public <T> List<T> perform( EntityManager em ) throws Exception
			{
		        Query q = em.createQuery( "SELECT FROM HelloWorld hw" );
		        List<T> worlds = q.getResultList();
		        for (T world : worlds)
		        {
		        	HelloWorld helloWorld = (HelloWorld)world; 
		        	
		        	for ( HelloMoon helloMoon : helloWorld.getHelloMoons() )
		        	{
		        		em.remove( helloMoon );
		        	}
		        	
		        	em.remove( helloWorld );
		        }

				return ( null );
			}
    	};
    	
    	DAOUtil.perform( op );
	}
}
