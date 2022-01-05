package com.example.lessonservice.service;

import android.os.AsyncTask;

public final class Mtask extends AsyncTask<Object,Object,Object> {
    private final String key;
    private final OnMTaskCallBack callBack;

    public Mtask(String key, OnMTaskCallBack callBack) {
        this.key = key;
        this.callBack = callBack;
    }

    public void requestUpdateUI(Object data){
        publishProgress(data);
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected final Object doInBackground(Object... objects) {
        if (objects==null){
            return callBack.executeTask(key,null,this);
        }else{
            return callBack.executeTask(key,objects[0],this);
        }
    }

    @Override
    protected final void onPostExecute(Object o) {
        callBack.completeTask(key,o);
    }

    @Override
    protected void onProgressUpdate(Object... values) {
        callBack.updateUI(key,values[0]);
    }
    public Mtask start(Object data){
        execute(data);
        return null;
    }
    public void startAsync(Object data){
        executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }
    public void stop(){
        cancel(true);
    }

    public interface OnMTaskCallBack{
        

        void completeTask(String key, Object o);

        default void updateUI(String key, Object value){}

        Object executeTask(String key, Object o, Mtask mtask);
    }
}
