package ch.noisette.doodle.services.impl.dummy;

import java.util.Collections;
import java.util.List;

import org.apache.cassandra.thrift.*;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import ch.noisette.doodle.domains.Poll;
import ch.noisette.doodle.domains.Subscriber;
import ch.noisette.doodle.services.PollService;
import java.io.UnsupportedEncodingException;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TFramedTransport;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

@Service
public class DummyPollServiceImpl implements PollService {
    
    
        private final static String DB_KEYSPACE = "doodle";
    	private final static String DB_HOST = "localhost";
        private final static int DB_PORT = 9160;
        private final static String UTF8 = "UTF-8";
        private Cassandra.Client client;
        private boolean isConnected = false;
        

	private static final Logger logger_c = Logger.getLogger(DummyPollServiceImpl.class);
        
        private void openDBconnection()throws TTransportException,
                UnsupportedEncodingException, InvalidRequestException, 
                NotFoundException, UnavailableException, TimedOutException, 
                TException, AuthenticationException, AuthorizationException {
            
            TTransport tr = new TFramedTransport(new TSocket(DB_HOST, DB_PORT));
            TProtocol protocol  = new TBinaryProtocol(tr);
            Cassandra.Client client = new Cassandra.Client(protocol);
            tr.open();
            client.set_keyspace(DB_KEYSPACE);
            isConnected = true;
        }
        
	@Override
	public Poll getPollById(String pollId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Poll> getAllPolls() {
		// TODO Auto-generated method stub
		return Collections.<Poll>emptyList();
	}

	@Override
	public Poll createPoll(Poll poll) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Poll addSubscriber(String pollId, Subscriber subscriber) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void deletePoll(String pollId) {
		// TODO Auto-generated method stub

	}



}
