package ch.noisette.doodle.services.impl.dummy;

import java.util.List;

import org.apache.cassandra.thrift.*;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import ch.noisette.doodle.domains.Poll;
import ch.noisette.doodle.domains.Subscriber;
import ch.noisette.doodle.services.PollService;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.UUID;
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
        private Cassandra.Client client;
        

	private static final Logger logger_c = Logger.getLogger(DummyPollServiceImpl.class);
        
        private synchronized void openDBconnection(){
            
            TTransport tr = new TFramedTransport(new TSocket(DB_HOST, DB_PORT));
            TProtocol protocol  = new TBinaryProtocol(tr);
            client = new Cassandra.Client(protocol);
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
            if(client != null){
                client.getOutputProtocol().getTransport().close();
            }
        }
        
        private void setkeyspace(String keyspace) {
            
            try {
                client.set_keyspace(keyspace);
            } catch (InvalidRequestException ex) {
                java.util.logging.Logger.getLogger(DummyPollServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("No such Keyspace available.");
            } catch (TException ex) {
                java.util.logging.Logger.getLogger(DummyPollServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }

                   


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
            openDBconnection();
            String pollID = UUID.randomUUID().toString();
            poll.setId(pollID);
            Column column = new Column();
            
            long timestamp = System.currentTimeMillis();
            ColumnParent columnParent = new ColumnParent("stats");
            try {
                
                column.setValue(poll.getLabel().toString().getBytes());
                column.setName("poll_name".getBytes());
                column.setTimestamp(timestamp);
            
                client.insert(makeByteBuffer(pollID), columnParent, column, ConsistencyLevel.ONE);
                
                
                column.setValue(poll.getChoices().toString().getBytes());
                column.setName("poll_choices".getBytes());
                column.setTimestamp(timestamp);
            
                client.insert(makeByteBuffer(pollID), columnParent, column, ConsistencyLevel.ONE);    
                
                column.setValue(poll.getEmail().toString().getBytes());
                column.setName("poll_creator_email".getBytes());
                column.setTimestamp(timestamp);
            
                client.insert(makeByteBuffer(pollID), columnParent, column, ConsistencyLevel.ONE);
                
                column.setValue(poll.getMaxChoices().toString().getBytes());
                column.setName("poll_max_choices".getBytes());
                column.setTimestamp(timestamp);
            
                client.insert(makeByteBuffer(pollID), columnParent, column, ConsistencyLevel.ONE);
                
                column.setValue(poll.getSubscribers().toString().getBytes());
                column.setName("poll_subscribers".getBytes());
                column.setTimestamp(timestamp);
            
                client.insert(makeByteBuffer(pollID), columnParent, column, ConsistencyLevel.ONE);
                
                } catch (InvalidRequestException ex) {
                    java.util.logging.Logger.getLogger(DummyPollServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                } catch (UnavailableException ex) {
                    java.util.logging.Logger.getLogger(DummyPollServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                } catch (TimedOutException ex) {
                    java.util.logging.Logger.getLogger(DummyPollServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                } catch (TException ex) {
                    java.util.logging.Logger.getLogger(DummyPollServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
                } catch (UnsupportedEncodingException ex) {
                    java.util.logging.Logger.getLogger(DummyPollServiceImpl.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
            System.out.println("Successfully created the poll with the id "+ pollID);
            closeDBconnection();
            return poll;
	}

	@Override
	public Poll addSubscriber(String pollId, Subscriber subscriber) {
		openDBconnection();
                setkeyspace(DB_KEYSPACE);

        try {

            ColumnParent cp = new ColumnParent("outgoers");
            long timestamp = System.currentTimeMillis();
            Column cLabel = new Column();
            cLabel.setTimestamp(timestamp);
            cLabel.setName(subscriber.getLabel().getBytes("UTF-8"));
            cLabel.setValue(subscriber.getChoices().toString().getBytes("UTF-8"));
            client.insert(ByteBuffer.wrap(pollId.getBytes("UTF-8")), cp, cLabel,
            ConsistencyLevel.QUORUM);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (TimedOutException e) {
            e.printStackTrace();
        } catch (InvalidRequestException e) {
            e.printStackTrace();
        } catch (UnavailableException e) {
            e.printStackTrace();
        } catch (TException e) {
            e.printStackTrace();
}

        Poll poll = new Poll();
        poll.setChoices(subscriber.getChoices());
        poll.setLabel(subscriber.getLabel());
        poll.setId(pollId);

        closeDBconnection();

        return poll;

}

	@Override
	public void deletePoll(String pollId) {
		// TODO Auto-generated method stub

	}

    private ByteBuffer makeByteBuffer(String toByte)
                throws UnsupportedEncodingException {
        return ByteBuffer.wrap(toByte.getBytes("UTF-8"));
}



}
