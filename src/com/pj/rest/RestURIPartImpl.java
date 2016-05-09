/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.pj.rest;

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 *
 * @author luzhenwen
 */
class RestURIPartImpl implements RestURIPart{
    private final String path;
    private final String operation;
    private final RestURICondition condition;
    private final String pathAlias;
    private RestURIPart parent;
    private final ArrayList<String> selectVars;
    
    public RestURIPartImpl( String path,String pathAlias, String operation, String condition) throws Exception{
        this.path = path;
        this.operation = operation;
        this.condition = condition == null?null:new RestURICondition(condition);
        this.pathAlias = pathAlias;
        selectVars = new ArrayList<String>(3);
        checkPath();
        checkOperation();
    }

    @Override
    public String toString() {
        return "/"+getPath()+"/"+getOperation()+"/"+(getCondition()==null?"":getCondition()); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * @return the operation
     */
    public String getOperation() {
        return operation;
    }

    public RestURICondition getCondition() {
        return condition;
    }

    public String getPathAlias() {
        return pathAlias;
    }

    public RestURIPart getParent() {
        return parent;
    }

    public void setParent(RestURIPart parent) {
        this.parent = parent;
    }

    public ArrayList<String> getSelectVars() {
        return selectVars;
    }

    private void checkOperation() {
        if (operation == null) {
            throw new IllegalArgumentException("illegal operation, operation should not be null");
        }
        if (!OPERATIONS.contains(operation)) {
            throw new IllegalArgumentException("illegal operation,operation must be one of:"+OPERATIONS);
        }
        
        if (operation.equalsIgnoreCase(OPERATION_ADD) && condition!=null) {
            throw new IllegalArgumentException("illegal operation,add operation can't have any condition");
        }
        
        if (operation.equalsIgnoreCase(OPERATION_GET) && (condition == null || !condition.isValid() || !condition.getFirstPart().isId())) {
            throw new IllegalArgumentException("illegal operation,get operation must indicate an id");
        }
    }

    private void checkPath() {
        if (Pattern.compile("[^\\w]").matcher(path).find()) {
            throw new IllegalArgumentException("illegal path,path can only contains numbers and characters");
        }
    }
    
    
}
