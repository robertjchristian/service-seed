package com.liaison.hellodao.dao;

import static org.junit.Assert.assertTrue;

import java.util.List;

import javax.persistence.EntityManager;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.liaison.commons.jpa.DAOUtil;
import com.liaison.commons.jpa.Op;
import com.liaison.commons.util.PrintUtil;
import com.liaison.commons.util.InitInitialContext;
import com.liaison.hellodao.model.HelloMoon;
import com.liaison.hellodao.model.HelloWorld;


public class HelloDAOTest
{
	static HelloDAO _dao = null;
	static InitTestData _initDao = null; 
	
	@BeforeClass
	public static void setUpBeforeClass() throws Exception
	{
		InitInitialContext.init();
		_dao = new HelloDAO();
		_initDao = new InitTestData(); 
		_initDao.initTestData();
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception
	{
		_initDao.deleteTestData();
	}

	@Before
	public void setUp() throws Exception
	{
	}

	@After
	public void tearDown() throws Exception
	{
	}
	
	@Test
	public void testFindWorlds() throws Exception
	{
    	long iTime = System.currentTimeMillis();
		
    	Op op = new Op()
    	{
			@SuppressWarnings("unchecked")
			@Override
			public <T> List<T> perform( EntityManager em ) throws Exception
			{
				List<T> list = (List<T>) _dao.findHelloWorld( em, normalizeSGUID( InitTestData.TEST_WORLD_SIID + "A" ) );
				return ( (List<T>) list );
			}
    	};
    	
    	List<Object []> list = DAOUtil.<Object []>fetch( op );
		
    	assertTrue( "list greater then zero",  list.size() > 0 );
    	
		for ( Object [] oar : list )
    	{
			HelloWorld helloWorld = null;
			HelloMoon helloMoon = null;
    		
    		for ( Object obj : oar )
	    	{
	        	if ( obj instanceof HelloWorld )
	            { 
	        		helloWorld = (HelloWorld)obj;
	            }
	            else if ( obj instanceof HelloMoon )
	            {
	            	helloMoon = (HelloMoon)obj;
	            }
	            else
	            {
	            	assertTrue( "Object Type Expected", false );
	            }
	    	}

        	assertTrue( "Fetched Expected Objects",  helloWorld != null && helloMoon != null );
    	}
	
    	PrintUtil.timePrint( "testFindWorlds", iTime );
	}
	
	@Test
	public void testFindWorldsAgain() throws Exception
	{
		testFindWorlds();
	}
	
	public static String normalizeSGUID( String strSGUID )
	{
		int iLength = 32 - strSGUID.length();
		for ( int i=0; i < iLength; i++ )
		{
			strSGUID += " ";
		}
		return ( strSGUID );
	}
}
