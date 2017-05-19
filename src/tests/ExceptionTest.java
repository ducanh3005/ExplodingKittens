package tests;

import org.junit.Test;

import exceptions.IncorrectCardIconIDException;
import exceptions.InvalidBundleException;

public class ExceptionTest {

	@Test(expected = IncorrectCardIconIDException.class)
	public void testIncorrectCardIconIDException() throws IncorrectCardIconIDException {
		throw new IncorrectCardIconIDException();
	}
	
	@Test(expected = IncorrectCardIconIDException.class)
	public void testIncorrectCardIconIDExceptionMessage() throws IncorrectCardIconIDException {
		throw new IncorrectCardIconIDException("Error");
	}
	
	@Test(expected = InvalidBundleException.class) 
	public void testInvalidBundleException() throws InvalidBundleException {
		throw new InvalidBundleException();
	}
	
	@Test(expected = InvalidBundleException.class) 
	public void testInvalidBundleExceptionMessage() throws InvalidBundleException {
		throw new InvalidBundleException("Error");
	}

}
