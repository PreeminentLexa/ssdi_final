package com.example.finalproject;

public class GameSettings {
    public GameSettings(){}

    private String[] keys = new String[1];
    private String[] types = new String[1];
    private String[] sValues = new String[1];
    private int[] iValues = new int[1];
    private float[] fValues = new float[1];
    private int settingCounts = 0;
    private void checkArraySize(){
        if(settingCounts < keys.length){return;}
        String[] newKeys = new String[keys.length*2];
        String[] newTypes = new String[types.length*2];
        String[] newSValues = new String[sValues.length*2];
        int[] newIValues = new int[iValues.length*2];
        float[] newFValues = new float[fValues.length*2];
        for(int i = 0;i < settingCounts;i++){
            newKeys[i] = keys[i];
            newTypes[i] = types[i];
            newSValues[i] = sValues[i];
            newIValues[i] = iValues[i];
            newFValues[i] = fValues[i];
        }
        keys = newKeys;
        types = newTypes;
        sValues = newSValues;
        iValues = newIValues;
        fValues = newFValues;
    }
    private void setSetting(int i, String type, String value){
        types[i] = type;
        switch(type){
            case "s":
                sValues[i] = value;
                break;
            case "i":
                iValues[i] = Integer.parseInt(value);
                break;
            case "f":
                fValues[i] = Float.parseFloat(value);
                break;
        }
    }
    public void handleSetting(String key, String type, String value){
        for(int i = 0;i < settingCounts;i++){
            if(key.equals(this.keys[i])){
                setSetting(i, type, value);
                return;
            }
        }
        checkArraySize();
        keys[settingCounts] = key;
        setSetting(settingCounts++, type, value);
    }
    public String getSSetting(String key){
        for(int i = 0;i < settingCounts;i++){
            if(key.equals(keys[i])){
                if(types[i].equals("s")){
                    return sValues[i];
                }
            }
        }
        return null;
    }
    public int getiSetting(String key){
        for(int i = 0;i < settingCounts;i++){
            if(key.equals(keys[i])){
                if(types[i].equals("i")){
                    return iValues[i];
                }
            }
        }
        return -1;
    }
    public float getfSetting(String key){
        for(int i = 0;i < settingCounts;i++){
            if(key.equals(keys[i])){
                if(types[i].equals("f")){
                    return fValues[i];
                }
            }
        }
        return -1;
    }
    public String getSettingType(String key){
        for(int i = 0;i < settingCounts;i++){
            if(key.equals(keys[i])){
                return types[i];
            }
        }
        return null;
    }
    public Object getUNKSetting(String key){
        for(int i = 0;i < settingCounts;i++){
            if(key.equals(keys[i])){
                switch(types[i]){
                    case "s":
                        return sValues[i];
                    case "i":
                        return iValues[i];
                    case "f":
                        return fValues[i];
                }
            }
        }
        return null;
    }
}
