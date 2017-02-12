package uk.ac.cam.cl.historyphone;

import java.lang.Exception;

// Generic "component initialisation failed" exception for
// non-recoverable errors when initialising server components (to
// avoid Server having to parse all possible exceptions types from
// various components)
class InitFailedException extends Exception {

	public InitFailedException(String msg, Throwable cause) {
		super(msg, cause);
	}
	
}
