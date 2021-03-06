//-----BEGIN DISCLAIMER-----
/*******************************************************************************
* Copyright (c) 2017 JCrypTool Team and Contributors
*
* All rights reserved. This program and the accompanying materials
* are made available under the terms of the Eclipse Public License v1.0
* which accompanies this distribution, and is available at
* http://www.eclipse.org/legal/epl-v10.html
*******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.merkletree.ui;

// import java.security.SecureRandom;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
// import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.jcryptool.visual.merkletree.Descriptions;
import org.jcryptool.visual.merkletree.algorithm.ISimpleMerkle;
import org.jcryptool.visual.merkletree.algorithm.SimpleMerkleTree;
import org.jcryptool.visual.merkletree.algorithm.XMSSTree;

/**
 * Composite for the Tabpage "Signatur"
 * 
 * @author Kevin Muehlboeck
 * @author Christoph Sonnberger
 *
 */
public class MerkleTreeSignatureComposite extends Composite {

    /**
     * Create the composite. Includes Message definition, Signature generation and Signature content
     * 
     * @param parent
     * @param style
     */
    Label sign;
    Text textSign;
    Button createSign;
    StyledText styledTextSign;
    StyledText styledTextSignSize;
    Label lSignaturSize;
    Label lkeyNumber;
    Label SingatureExpl;
    Label descLabel;
    String signature = null;

    StyledText styledTextKeyNumber;
    ISimpleMerkle merkle;

    public MerkleTreeSignatureComposite(Composite parent, int style, ISimpleMerkle merkle) {
        super(parent, SWT.NONE);
        this.setLayout(new GridLayout(MerkleConst.H_SPAN_MAIN, true));

        this.merkle = merkle;

        descLabel = new Label(this, SWT.NONE);
        descLabel.setLayoutData(new GridData(SWT.RIGHT, SWT.CENTER, true, false, MerkleConst.H_SPAN_MAIN, 1));

        sign = new Label(this, SWT.NONE);
        sign.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, MerkleConst.H_SPAN_MAIN, 1));
        sign.setText(Descriptions.MerkleTreeSign_0);

        textSign = new Text(this, SWT.BORDER | SWT.V_SCROLL | SWT.CANCEL | SWT.MULTI);
        GridData gd_textSign = new GridData(SWT.FILL, SWT.FILL, true, true, MerkleConst.H_SPAN_MAIN, 1);
        textSign.setLayoutData(gd_textSign);
        textSign.setText(Descriptions.MerkleTreeSign_1);
        createSign = new Button(this, SWT.NONE);
        createSign.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, MerkleConst.H_SPAN_MAIN / 2, 1));
        createSign.setText(Descriptions.MerkleTreeSign_2);

        lkeyNumber = new Label(this, SWT.READ_ONLY | SWT.WRAP | SWT.RIGHT);
        lkeyNumber.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, MerkleConst.H_SPAN_MAIN / 5, 1));
        lkeyNumber.setText(Descriptions.MerkleTreeSign_7);

        styledTextKeyNumber = new StyledText(this, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP);
        styledTextKeyNumber
                .setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, MerkleConst.H_SPAN_MAIN / 5, 1));
        styledTextKeyNumber.setText("");

        lSignaturSize = new Label(this, SWT.READ_ONLY | SWT.WRAP | SWT.RIGHT);
        lSignaturSize.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, MerkleConst.H_SPAN_MAIN / 5, 1));
        lSignaturSize.setText(Descriptions.MerkleTreeSign_6);

        styledTextSignSize = new StyledText(this, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP);
        styledTextSignSize
                .setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, MerkleConst.H_SPAN_MAIN / 5, 1));
        styledTextSignSize.setText("");

        SingatureExpl = new Label(this, SWT.NONE);
        SingatureExpl.setLayoutData(new GridData(SWT.LEFT, SWT.CENTER, true, false, MerkleConst.H_SPAN_MAIN, 1));

        /**
         * @author Christoph Sonnberger The Text is based on the used suite if there will
         *         implemented an other suite, just add an else if and type the name of the instance
         *         Example for MultiTree -> merkle instanceof XMSSMT
         */
        if (merkle instanceof XMSSTree) {
            SingatureExpl.setText(Descriptions.XMSS.Tab2_Txt0);
            SingatureExpl.setText(Descriptions.XMSS.Tab2_Txt0);
            descLabel.setText(Descriptions.XMSS.Tab1_Head0);
        } else if (merkle instanceof SimpleMerkleTree) {
            SingatureExpl.setText(Descriptions.MSS.Tab2_Txt0);
            SingatureExpl.setText(Descriptions.MSS.Tab2_Txt0);
            descLabel.setText(Descriptions.MSS.Tab1_Head0);

        }

        styledTextSign = new StyledText(this, SWT.BORDER | SWT.READ_ONLY | SWT.WRAP | SWT.MULTI | SWT.V_SCROLL);
        GridData gd_textTextSign = new GridData(SWT.FILL, SWT.FILL, true, true, MerkleConst.H_SPAN_MAIN, 1);
        styledTextSign.setLayoutData(gd_textTextSign);

        createSign.addSelectionListener(new SelectionAdapter() {

            /*
             * Event to create a Signature
             */
            @Override
            public void widgetSelected(SelectionEvent e) {
                /*
                 * store signature in temp string, to verify it
                 */
                String temp = merkle.sign(textSign.getText());
                if (temp != "") {
                    signature = temp;
                    /**
                     * updated the field of the Signature, KeyIndex and SignatureLength
                     */
                    styledTextSign.setText(signature);
                    styledTextSignSize.setText(getSignatureLength(signature) + " Byte");
                    styledTextKeyNumber.setText(getKeyIndex(signature));
                } else {
                    styledTextSign.setText("No more Keypairs available");
                }
            }
        });
        textSign.addModifyListener(new ModifyListener() {
            @Override
            public void modifyText(ModifyEvent e) {
                if (textSign.getText().length() > 0) {
                    createSign.setEnabled(true);
                } else {
                    createSign.setEnabled(false);
                }

            }
        });
    }

    /**
     * Synchronizes Signature with the other Tabpages
     * 
     * @return Signature
     */
    public String getSignature() {
        return signature;
    }

    /**
     * Return the used Message necessary for tab sync -> verification tab
     * 
     * @return usedText
     */
    public String getMessage() {
        return textSign.getText();
    }

    /**
     * @author christoph sonnberger returns the Length of the Siganture as String used for
     *         styledTextSignSize in GUI
     * @param signature
     * @return length of the Signature
     */
    public String getSignatureLength(String signature) {
        int length = signature.length();
        // divide by 2 to get the length in bytes
        length = length / 2;
        StringBuilder sb = new StringBuilder();
        sb.append("");
        sb.append(length);
        String sigLength = sb.toString();
        return sigLength;
    }

    /**
     * @author christoph sonnberger returns the Index of a given signature as String the Index is
     *         the first Letter of the signature
     * @param signature
     * @return index
     */
    public String getKeyIndex(String signature) {
        int iend = signature.indexOf("|");
        String subString = signature.substring(0, iend);
        return subString;
    }

    /**
     * Synchronizes the MerkleTree Object with the other Tabpages
     * 
     * @return ISimpleMerkle Object
     */
    public ISimpleMerkle getMerkleFromForm() {
        return this.merkle;
    }

    @Override
    protected void checkSubclass() {
        // Disable the check that prevents subclassing of SWT components
    }

}
