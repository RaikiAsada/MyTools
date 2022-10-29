/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db.structure.document;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

/**
 * Documentをこのツールで
 */
public abstract class GeneralXml {
    protected final Document document;

    public GeneralXml(Document document) {
        this.document = document;
    }
    public GeneralXml appendElement(String elementName) {
        Element tag = document.createElement(elementName);
        this.getNode().appendChild(tag);
        return this;
    }
    
    public GeneralXml appendElement(String elementName, String text) {
        Element tableNameTag = document.createElement(elementName);
        tableNameTag.appendChild(document.createTextNode(text));
        this.getNode().appendChild(tableNameTag);
        return this;
    }
    
    abstract Node getNode();
}
