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
        final List<GuessItUserData> guessItUserDataList = new ArrayList<>();
        final List<String> userPropertiesUidList = new ArrayList<>();
        if(mAuth != null){
            listener.onProgress("Veriler çekiliyor.");
            FirebaseDatabase.getInstance().getReference("AppType").child("isBeta").addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        if (dataSnapshot.getValue(Boolean.class)){
                            FirebaseDatabase.getInstance().getReference("Dataset").addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    for (DataSnapshot ds:dataSnapshot.getChildren()){
                                        String username = ds.child("displayName").getValue(String.class);
                                        String photoUrl = ds.child("photoUrl").getValue(String.class);
                                        Integer age = ds.child("age").getValue(Integer.class);
                                        String uid = ds.getKey();
                                        GuessItUserData guessItUserData = new GuessItUserData(uid,age,photoUrl,username);
                                        guessItUserDataList.add(guessItUserData);
                                        if(guessItUserDataList.size() == dataSnapshot.getChildrenCount()){
                                            Collections.shuffle(guessItUserDataList);
                                            listener.onSuccess(guessItUserDataList,true);
                                        }
                                    }

                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }else
                        {
                            FirebaseDatabase.getInstance().getReference(ApprovedUsers).limitToLast(50).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull final DataSnapshot dataSnapshotApproved) {
                                    FirebaseDatabase.getInstance().getReference(CorrectUsers).child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot dataSnapshotCorrect) {
                                            if(dataSnapshotCorrect.getChildrenCount() < 2){
                                                if(dataSnapshotApproved.getChildrenCount() > 3){
                                                    for (DataSnapshot ds:dataSnapshotApproved.getChildren()){
                                                        if(guessItUserDataList.size() < 3 && !ds.getKey().equals(mAuth.getUid())){
                                                            guessItUserDataList.add(ds.getValue(GuessItUserData.class));
                                                        }else if(ds.getKey().equals(mAuth.getUid())){
                                                            continue;
                                                        }
                                                        if(guessItUserDataList.size() == 3){
                                                            listener.onProgress("Yarışma ayarları yapılandırılıyor.");
                                                            listener.onProgress("Yarışma Hazır...");
                                                            listener.onSuccess(guessItUserDataList,false);
                                                            break;
                                                        }
                                                    }
                                                }
                                            }else {
                                                if(dataSnapshotApproved.getChildrenCount() > 3){
                                                    listener.onProgress("Uygunsuz kullanıcılar ayıklandı.");
                                                    for (DataSnapshot ds:dataSnapshotApproved.getChildren()){
                                                        if(!dataSnapshotCorrect.hasChild(ds.getKey())){
                                                            if(guessItUserDataList.size() <= 3 &&!ds.getKey().equals(mAuth.getUid())){
                                                                guessItUserDataList.add(ds.getValue(GuessItUserData.class));
                                                            }else {
                                                                continue;
                                                            }
                                                        }
                                                    }
                                                    if(guessItUserDataList.size() == 0){
                                                        FirebaseDatabase.getInstance().getReference(ApprovedUsers).limitToLast(100).addListenerForSingleValueEvent(new ValueEventListener() {
                                                            @Override
                                                            public void onDataChange(@NonNull final DataSnapshot dataSnapshotApproved) {
                                                                FirebaseDatabase.getInstance().getReference(CorrectUsers).child(mAuth.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                                                    @Override
                                                                    public void onDataChange(@NonNull DataSnapshot dataSnapshotCorrect) {
                                                                        if(dataSnapshotCorrect.getChildrenCount() < 2){
                                                                            if(dataSnapshotApproved.getChildrenCount() > 3){
                                                                                for (DataSnapshot ds:dataSnapshotApproved.getChildren()){
                                                                                    if(guessItUserDataList.size() < 3 && !ds.getKey().equals(mAuth.getUid())){
                                                                                        guessItUserDataList.add(ds.getValue(GuessItUserData.class));
                                                                                    }else if(ds.getKey().equals(mAuth.getUid())){
                                                                                        continue;
                                                                                    }
                                                                                    if(guessItUserDataList.size() == 3){
                                                                                        listener.onProgress("Yarışma ayarları yapılandırılıyor.");
                                                                                        listener.onProgress("Yarışma Hazır...");
                                                                                        listener.onSuccess(guessItUserDataList,false);
                                                                                        break;
                                                                                    }
                                                                                }
                                                                            }
                                                                        }else {
                                                                            if(dataSnapshotApproved.getChildrenCount() > 3){
                                                                                listener.onProgress("Uygunsuz kullanıcılar ayıklandı.");
                                                                                for (DataSnapshot ds:dataSnapshotApproved.getChildren()){
                                                                                    if(!dataSnapshotCorrect.hasChild(ds.getKey())){
                                                                                        if(guessItUserDataList.size() <= 3 &&!ds.getKey().equals(mAuth.getUid())){
                                                                                            guessItUserDataList.add(ds.getValue(GuessItUserData.class));
                                                                                        }else {
                                                                                            continue;
                                                                                        }
                                                                                    }
                                                                                }
                                                                                if (guessItUserDataList.size() !=0)
                                                                                listener.onSuccess(guessItUserDataList,false);
                                                                                else listener.onFailed();
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
                                                    }else {
                                                        listener.onSuccess(guessItUserDataList,false);
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
