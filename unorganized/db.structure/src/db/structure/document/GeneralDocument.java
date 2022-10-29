/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package db.structure.document;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;


public class GeneralDocument {
    private Document document;
    private Node node;
    
    public GeneralDocument appendElement(String elementName) {
        Element tag = document.createElement(elementName);
        node.appendChild(tag);
        return this;
    }
    
    public GeneralDocument appendElement(String elementName, String text) {
        Element tableNameTag = document.createElement(elementName);
        tableNameTag.appendChild(document.createTextNode(text));
        node.appendChild(tableNameTag);
        return this;
    }
}
