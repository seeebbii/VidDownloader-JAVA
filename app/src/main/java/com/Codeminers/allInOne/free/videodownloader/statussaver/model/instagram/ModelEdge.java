package com.Codeminers.allInOne.free.videodownloader.statussaver.model.instagram;

import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class ModelEdge implements Serializable {

    @SerializedName("node")
    private ModelNode modelNode;

    public ModelNode getModelNode() {
        return modelNode;
    }

    public void setModelNode(ModelNode modelNode) {
        this.modelNode = modelNode;
    }
}
