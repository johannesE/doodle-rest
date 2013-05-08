package ch.noisette.doodle.services.impl.dummy;

import java.util.Collections;
import java.util.List;

import org.apache.cassandra.thrift.*;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import ch.noisette.doodle.domains.Poll;
import ch.noisette.doodle.domains.Subscriber;
import ch.noisette.doodle.services.PollService;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.logging.Level;
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
        private TTransport tr;
        

	private static final Logger logger_c = Logger.getLogger(DummyPollServiceImpl.class);
        
        private synchronized void openDBconnection(){
            
            TTransport tr = new TFramedTransport(new TSocket(DB_HOST, DB_PORT));
            TProtocol protocol  = new TBinaryProtocol(tr);
            Cassandra.Client client = new Cassandra.Client(protocol);
            try {
                tr.open();
            } catch (TTransportException ex) {
                java.util.logging.Logger.getLogger(DummyPollServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
            try {
                client.set_keyspace(DB_KEYSPACE);
            } catch (InvalidRequestException ex) {
                java.util.logging.Logger.getLogger(DummyPollServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            } catch (TException ex) {
                java.util.logging.Logger.getLogger(DummyPollServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }

        }
        
        private void closeDBconnection() {
        tr.close();
        }
        
	@Override
	public Poll getPollById(String pollId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<Poll> getAllPolls() {
            List<Poll> polls = new LinkedList<Poll>();
            openDBconnection();
            KeyRange keyRange = new KeyRange(Integer.MAX_VALUE);
            keyRange.setStart_key(new byte[0]);
            keyRange.setEnd_key(new byte[0]);
            
            SlicePredicate slicePredicate = new SlicePredicate();
            ColumnParent columnParent = new ColumnParent("stats");
            
            try {
                List<KeySlice> keySlices = client.get_range_slices(columnParent, slicePredicate, keyRange, ConsistencyLevel.ONE);
                
                for (Iterator<KeySlice> iterator = keySlices.iterator(); iterator.hasNext(); ){
                    KeySlice keySlice = iterator.next();
                   polls.add(getPollById(new String(keySlice.getKey())));
                }
                
            } catch (InvalidRequestException ex) {
                java.util.logging.Logger.getLogger(DummyPollServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            } catch (UnavailableException ex) {
                java.util.logging.Logger.getLogger(DummyPollServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            } catch (TimedOutException ex) {
                java.util.logging.Logger.getLogger(DummyPollServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            } catch (TException ex) {
                java.util.logging.Logger.getLogger(DummyPollServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
            closeDBconnection();
            return polls;
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
