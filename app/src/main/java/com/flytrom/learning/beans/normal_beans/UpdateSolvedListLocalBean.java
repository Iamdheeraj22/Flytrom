package com.flytrom.learning.beans.normal_beans;

import com.flytrom.learning.beans.response_beans.others.SolveMCQBean;

import java.util.List;

public class UpdateSolvedListLocalBean {

    private List<SolveMCQBean> resultModel;
    private int id;

    public List<SolveMCQBean> getResultModel() {
        return resultModel;
    }

    public void setResultModel(List<SolveMCQBean> resultModel) {
        this.resultModel = resultModel;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
