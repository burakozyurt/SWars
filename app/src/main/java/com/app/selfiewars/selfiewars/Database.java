package com.app.selfiewars.selfiewars;

import android.support.annotation.NonNull;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Database {
    public Database() {
    }

    public void mReadDataOnce(String child, final OnGetDataListener listener) {
        listener.onStart();
        FirebaseDatabase.getInstance().getReference(child).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listener.onSuccess(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onFailed(databaseError);
            }
        });
    }
    public void mReadDataRealTime(String child, final OnGetDataListener listener) {
        listener.onStart();
        FirebaseDatabase.getInstance().getReference(child).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                listener.onSuccess(dataSnapshot);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                listener.onFailed(databaseError);
            }
        });
    }
    public void mReadDataAndGetUserListForGuessIt(final FirebaseAuth mAuth, final String ApprovedUsers, final String CorrectUsers, final OnGetUserlistDataListener listener){
        listener.onStart();
        final List<UserProperties> userPropertiesList = new ArrayList<>();
        final List<String> userPropertiesUidList = new ArrayList<>();
        if(mAuth != null){
            FirebaseDatabase.getInstance().getReference("AppType").child("isBeta").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue(Boolean.class)){
                            FirebaseDatabase.getInstance().getReference("Dataset").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot ds:dataSnapshot.getChildren()){
                                        String diplayName = ds.child("displayName").getValue(String.class);
                                        String photoUrl = ds.child("photoUrl").getValue(String.class);
                                        Integer age = ds.child("age").getValue(Integer.class);

                                        UserProperties userProperties = new UserProperties(null,null,diplayName,photoUrl,age);
                                        userPropertiesList.add(userProperties);
                                        if(userPropertiesList.size() == dataSnapshot.getChildrenCount()){
                                            Collections.shuffle(userPropertiesList);
                                            listener.onSuccess(userPropertiesList);
                                        }
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }else
                        {
                            FirebaseDatabase.getInstance().getReference(ApprovedUsers).limitToLast(100).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull final DataSnapshot dataSnapshotApproved) {
                                    FirebaseDatabase.getInstance().getReference(CorrectUsers).child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshotCorrect) {
                                            if(dataSnapshotCorrect.getChildrenCount() < 2){
                                                if(dataSnapshotApproved.getChildrenCount() > 3){
                                                    for (DataSnapshot ds:dataSnapshotApproved.getChildren()){
                                                        if(userPropertiesUidList.size() < 3 && !ds.getKey().equals(mAuth.getUid())){
                                                            userPropertiesUidList.add(ds.getKey());
                                                            //listener.onProgress("Approved retrieve Kullanıcı : " + ds.getKey());

                                                        }else if(ds.getKey().equals(mAuth.getUid())){
                                                            //listener.onProgress("Approved retrieve kendimi buldum size:" + userPropertiesUidList.size());
                                                            continue;
                                                        }
                                                        if(userPropertiesUidList.size() == 3){
                                                            listener.onProgress("Approved retrieve bitirdim size:" + userPropertiesUidList.size());
                                                            FirebaseDatabase.getInstance().getReference("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                @Override
                                                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                    for (String uid:userPropertiesUidList){
                                                                        UserProperties userProperties = dataSnapshot.child(uid).child("properties").getValue(UserProperties.class);
                                                                        //userProperties.setPhotoUrl(uid);
                                                                        userPropertiesList.add(userProperties);
                                                                        if (userPropertiesList.size()==userPropertiesUidList.size()){
                                                                            Collections.shuffle(userPropertiesList);
                                                                            listener.onSuccess(userPropertiesList);
                                                                            break;
                                                                        }
                                                                    }

                                                                }

                                                                @Override
                                                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                                                    listener.onFailed();

                                                                }
                                                            });
                                                            break;
                                                        }
                                                    }
                                                }
                                            }else {
                                                if(dataSnapshotApproved.getChildrenCount() > 3){
                                                    listener.onProgress("çocuk sayısı 3 ten büyük");

                                                    for (DataSnapshot ds:dataSnapshotApproved.getChildren()){
                                                        if(!dataSnapshotCorrect.child(mAuth.getUid()).hasChild(ds.getKey())){
                                                            if(userPropertiesUidList.size() <= 3 &&!ds.getKey().equals(mAuth.getUid())){
                                                                userPropertiesUidList.add(ds.getKey());
                                                            }else {
                                                                listener.onProgress("Approved retrieve");
                                                                FirebaseDatabase.getInstance().getReference("Users").addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                                                        for (String uid:userPropertiesUidList){
                                                                            UserProperties userProperties =dataSnapshot.child(uid).child("properties").getValue(UserProperties.class);
                                                                            //userProperties.setPhotoUrl(uid);                                                            userPropertiesList.add(userProperties);
                                                                            if (userPropertiesList.size()==userPropertiesUidList.size()){
                                                                                listener.onSuccess(userPropertiesList);
                                                                                break;
                                                                            }
                                                                        }

                                                                    }
                                                                    @Override
                                                                    public void onCancelled(@NonNull DatabaseError databaseError) {
                                                                        listener.onFailed();

                                                                    }
                                                                });
                                                                break;
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError databaseError) {
                                            listener.onFailed();
                                        }
                                    });
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {
                                    listener.onFailed();
                                }
                            });

                        }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });


        }else listener.onFailed();

    }

}
