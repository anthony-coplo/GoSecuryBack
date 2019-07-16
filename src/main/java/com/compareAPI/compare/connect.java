package com.compareAPI.compare;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.ExecutionException;

import com.google.api.core.ApiFuture;
import com.google.api.gax.rpc.ApiException;
import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.firestore.Firestore;
import com.google.cloud.firestore.FirestoreOptions;
import com.google.cloud.firestore.QueryDocumentSnapshot;
import com.google.cloud.firestore.QuerySnapshot;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;

import Entity.Image;
import Entity.Ressources;;


public class connect {

    FirestoreOptions storeOption = null;
    
	public void Connection() {
		
		
		if(FirebaseApp.getApps().size() > 0) {
			FirebaseApp.getInstance().delete();
		}
		
	    FileInputStream serviceAccount = null;
	    
	    FirebaseApp app = null;
	    
	    String jsFname = "src/main/resources/auth.json";
	    String jsProject = null;
	
	    try {
	
	        serviceAccount = openJsonFile( jsFname );
	
	        FirebaseOptions options = new FirebaseOptions.Builder()
	                .setCredentials( GoogleCredentials.fromStream( serviceAccount ))
	                .setDatabaseUrl( "https://gosecuryfrontdb.firebaseio.com" ).build();
	
	        app = FirebaseApp.initializeApp( options );
	
	    } catch ( FileNotFoundException e ) {
	        System.out.println( "ERROR While get Google service :" + e );
	    } catch ( IOException ex ) {
	        System.out.println( "ERROR While get Google service :" + ex );
	    }    
	    
	
	    try {
	        serviceAccount = openJsonFile( jsFname );
	        storeOption = FirestoreOptions.newBuilder()
	                .setTimestampsInSnapshotsEnabled( true ).setProjectId( jsProject )
	                .setCredentials( GoogleCredentials.fromStream( serviceAccount ) )
	                .build();
	    } catch ( IOException e1 ) {
	        System.out.println( "ERROR While FirestoreOptions BUILD : " + e1.getLocalizedMessage() );
	    }

	}
	
	private  FileInputStream openJsonFile(String fname) throws FileNotFoundException {
		FileInputStream serviceAccount = new FileInputStream(fname);
		return serviceAccount;
	}                
	
	public Collection<Image> getUsers() throws Exception {
		Firestore store = storeOption.getService();
		ApiFuture<QuerySnapshot> query = store.collection("images").orderBy("id").get();
        QuerySnapshot querySnapshot = null ;
        try {
            querySnapshot = query.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new Exception("ERROR While QuerySnapshot Get :"+e.getLocalizedMessage());
        }
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        Collection<Image> usersToReturn = new ArrayList<>();
        documents.forEach(i-> {
            Image image = i.toObject(Image.class);
            usersToReturn.add(image);
            
            System.out.println(image);

        });
        
        return usersToReturn;
	}
	 
	public Collection<Ressources> getRessources() throws Exception {
		Firestore store = storeOption.getService();
		ApiFuture<QuerySnapshot> query = store.collection("ressources").get();
        QuerySnapshot querySnapshot = null ;
        try {
            querySnapshot = query.get();
        } catch (InterruptedException | ExecutionException e) {
            throw new Exception("ERROR While QuerySnapshot Get :"+e.getLocalizedMessage());
        }
        List<QueryDocumentSnapshot> documents = querySnapshot.getDocuments();
        Collection<Ressources> ressourcesToReturn = new ArrayList<>();
        documents.forEach(r-> {
            Ressources ressource = r.toObject(Ressources.class);
            ressourcesToReturn.add(ressource);
            
            System.out.println(ressource);

        });
        
        return ressourcesToReturn;
	}
}

