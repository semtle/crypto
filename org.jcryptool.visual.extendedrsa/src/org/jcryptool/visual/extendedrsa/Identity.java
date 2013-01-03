//-----BEGIN DISCLAIMER-----
/*******************************************************************************
 * Copyright (c) 2012 JCrypTool Team and Contributors
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
//-----END DISCLAIMER-----
package org.jcryptool.visual.extendedrsa;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;
import static org.jcryptool.visual.library.Lib.LOW_PRIMES;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.Vector;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.KeyEvent;
import org.eclipse.swt.events.KeyListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.MouseEvent;
import org.eclipse.swt.events.MouseMoveListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.events.VerifyListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.TabFolder;
import org.eclipse.swt.widgets.TabItem;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.wb.swt.SWTResourceManager;
import org.jcryptool.core.util.fonts.FontService;
import org.jcryptool.crypto.keystore.backend.KeyStoreAlias;
import org.jcryptool.visual.library.Constants;
import org.jcryptool.visual.library.Lib;

/**
 * This class represents an identity in the visual.
 * @author Christoph Schnepf, Patrick Zillner
 *
 */
public class Identity extends TabItem {

	private String identityName;
	private String forename;
	private String surname;
	private String organisation;
	private String region;
	private Group generalGroup;
	private Composite composite;
	private Label label;
	private Button enc_and_send;
	private Group actionGroup_1;
	private Group actionGroup_2;
	private Group actionGroup_3;
	private Group actionGroup_4;
	private GridLayout actionLayout_1;
	private GridLayout actionLayout_2;
	private GridLayout actionLayout_3;
	private GridLayout actionLayout_4;
	private Label initActions;
	private Label initActions2;
	private Text subjectInput;
	private Text clearMessage;
	private Text encryptedMessage;
	private Text decryptedMessage;
	private Text encryptedMessage_Tab2;
	private Combo messageRecipient;
	private Button sendMessage;
	private Button encryptMessage;
	private Button decryptMessage;
	private Combo recipientKeys;
	private Button receive_and_decrypt;
	private Button attackPublicKey;
	private Button keymanagement;
	private GridData group_1;
	private GridData group_2;
	private GridData group_3;
	private GridData group_4;
	private int forerunner;
	private int id;
	private Combo subjectChoose;
	private Combo decryptionKey;
	private Button deleteMessage;
	private Text pwPrivKey;
	private TabFolder tabfolder;
	private TabFolder tf_keyMgmt;
	private TabItem keyMgmt_1;
	private TabItem keyMgmt_2;
	private TabItem keyMgmt_3;
	private Button radio_RSA;
	private Button radio_ExtRSA;
	private Button radio_RSA_tab2;
	private Button radio_ExtRSA_tab2;
	private Composite tab1;
	private Composite tab2;
	private Composite tab3;
	private Combo combo_rsaP;
	private Combo combo_rsaQ;
	private Combo combo_rsaE;
	private BigInteger bi_rsaP;
	private BigInteger bi_rsaQ;
	private BigInteger bi_rsaE;
	private BigInteger bi_rsaN;
	private BigInteger bi_rsaPhi;
	private BigInteger bi_ExtrsaP;
	private BigInteger bi_ExtrsaQ;
	private BigInteger bi_ExtrsaR;
	private BigInteger bi_ExtrsaS;
	private BigInteger bi_ExtrsaT;
	private BigInteger bi_ExtrsaE;
	private BigInteger bi_ExtrsaN;
	private BigInteger bi_ExtrsaPhi;
	private Button pickRandomE;
	private Button pickRandomExtE;
	private Vector<BigInteger> possibleEs;
	private Label errorLabel_1;
	private boolean pIsPrime;
	private boolean qIsPrime;
	private boolean rIsPrime;
	private boolean sIsPrime;
	private boolean tIsPrime;
	private boolean eIsValid;
	private boolean init;
	private Composite rsaComposite;
	private Composite rsaExMainComposite;
	private Composite rsaExComposite1;
	private Composite rsaExComposite2;
	private Composite rsaExComposite3;
	private Combo numberOfPrimesExRSA;
	private Combo combo_ExrsaP;
	private Combo combo_ExrsaQ;
	private Combo combo_ExrsaE;
	private Combo combo_ExrsaR;
	private Combo combo_ExrsaS;
	private Combo combo_ExrsaT;
	private Label rsa_ex_S;
	private Label rsa_ex_T;
	private Text password1;
	private Text password2;
	private Text ext_password1; 
	private Text ext_password2;
	private Combo rsa_length;
	private Combo extRsa_length;
	private Combo extRsa_numberPrimes_tab2;
	private Button createKey;
	private Button createKey_Tab2;
	private Combo selectedKey_Keydata;
	private Text password_keydata;
	private Button showKeydata;
	private Table keyData;
	private TableColumn column_parameter;
	private TableColumn column_value;
	private Label txtExplain;
	private Label init_tab1;
	private String pw1;
	private String pw2;
	private String pw1_Ext;
	private String pw2_Ext;
	private int validCount;
	private IdentityManager iMgr;
	private HashMap<String, KeyStoreAlias> rec;
	
    /** a {@link VerifyListener} instance that makes sure only digits are entered. */
    private static final VerifyListener VL = Lib.getVerifyListener(Lib.DIGIT);

    /** a {@link ModifyListener} instance that calls {@link #calcParams()} whenever a value is changed. */
    private final ModifyListener ml = new ModifyListener() {
        public void modifyText(ModifyEvent e) {
                try {
                    bi_rsaP = new BigInteger(combo_rsaP.getText());
                    bi_rsaQ = new BigInteger(combo_rsaQ.getText());
                    checkParameter();
                } catch (NumberFormatException nfe) {
                	bi_rsaN = Constants.MINUS_ONE;
                	bi_rsaPhi =Constants.MINUS_ONE;
                }
            }
    };
	
	public Identity(TabFolder parent, int style, String identityName, String forename, String surname, String organisation, String region, Label explain) {
		super(parent, style);
		this.tabfolder = parent;
		this.identityName = identityName;
		this.forename = forename;
		this.surname = surname;
		this.organisation = organisation;
		this.region = region;
		this.id = parent.getItemCount();
		this.txtExplain = explain;
		
		iMgr = new IdentityManager();
		
		//set the text of the TabItem
		this.setText(identityName);
		forerunner = 0;	
		init = true;
		
		
		txtExplain.setText("Die Identitäten aus Ihrem Schlüsselspeicher werden in dieser Visualisierung als Tabs (Registerkarten) angezeigt. Schon bei der Auslieferung befinden sich die Identitäten „Alice“ und „Bob“ im Schlüsselspeicher und werden dehalb auch initial schon als Tabs angezeigt.\n\nJede Registerkarte stellt eine Identität dar. Durch den Button „Identitäten ein-/ausblenden“ können bestehende Identitäten als Registerkarten angezeigt oder ausgeblendet werden. Wenn eine neue Identität erstellt wird, wird diese erst als Registerkarte angezeigt, wenn sie durch „Identitäten ein-/ausblenden“ ausgewählt wurde!\n\nWird nun ein Button auf einer Registerkarte angeklickt (und so eine Aktion im Namen einer Identität durchgeführt), wird eine Hilfe im Feld „Erklärungen“ angezeigt.");
		
		// define the layout for the whole TabItem now
		generalGroup = new Group(parent, SWT.NONE);
		generalGroup.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, true, false, 1, 1));
		
		//2 columns (actions and the actionswindow)
		generalGroup.setLayout(new GridLayout(2, false));
		this.setControl(generalGroup);
		
		//Grid-Layout for all the buttons on the left side
		composite = new Composite(generalGroup, SWT.NONE);
		composite.setLayout(new GridLayout(1, true));
		composite.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
		
		label = new Label(composite, SWT.CENTER);
		label.setFont(FontService.getNormalBoldFont());
		label.setText("Aktionen:");
		label.setLayoutData(new GridData(SWT.CENTER, SWT.CENTER, false,false, 1, 1));
		
		new Label(composite, SWT.SEPARATOR | SWT.HORIZONTAL).setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
		
		enc_and_send = new Button(composite, SWT.PUSH);
		enc_and_send.addSelectionListener(new SelectionAdapter() {
			@Override
			//Button 1
			public void widgetSelected(final SelectionEvent e) {
				txtExplain.setText("Für den Verschlüsselungsvorgang werden die Parameter N und e benötigt. Mehr Informationen zu den einzelnen Parametern finden Sie in der Registerkarte „Meine Schlüssel“  in der „Schlüsselverwaltung“.\n\n Vorgehensweise:\n1) Geben Sie eine beliebige Nachricht und einen optionalen Betreff ein.\n\n2) Wählen Sie einen Empfänger aus (zum Beispiel Bob). Hinweis: Diese Visualisierung erlaubt nur einen Empfänger. In der Praxis eingesetzte Protokolle wie S/MIME erlauben auch, eine Nachricht an mehr als einen Empfänger gleichzeitig verschlüsselt zu senden.\n\n3) Wählen Sie einen öffentlichen Schlüssel des Empfängers aus.\n\n4) Klicken Sie auf „Nachricht verschlüsseln“, um die Nachricht zu verschlüsseln.\n\n5) Die verschlüsselte Nachricht erscheint nun im Textfeld rechts („Verschlüsselte Nachricht“).\n\n6) Klicken Sie auf „Senden“, um die verschlüsselte Nachricht zu verschicken.");
				
				if(actionGroup_1.isDisposed()){
					createActionGroup1();
				}
				if (forerunner != 1){
					actionGroup_2.dispose();
					actionGroup_3.dispose();
					actionGroup_4.dispose();
					
					initActions.setText("Betreff der Nachricht: ");
					createSpacer(actionGroup_1);
					
					subjectInput = new Text(actionGroup_1, SWT.BORDER);
					GridData gd_subject = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
					gd_subject.heightHint = 20;
					subjectInput.setLayoutData(gd_subject);
					createSpacer(actionGroup_1);
					
					label = new Label (actionGroup_1, SWT.NONE);
					label.setText("Nachricht:");
					GridData gd_message = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
					gd_message.heightHint = 20;
					label.setLayoutData(gd_message);
					
					label = new Label (actionGroup_1, SWT.NONE);
					label.setText("Verschl\u00fcsselte Nachricht:");
					GridData gd_enc_message = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
					gd_enc_message.heightHint = 20;
					label.setLayoutData(gd_enc_message);
					
					clearMessage = new Text(actionGroup_1, SWT.MULTI | SWT.WRAP | SWT.BORDER);
					clearMessage.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 33));
					clearMessage.addModifyListener(new ModifyListener() {
						
						@Override
						public void modifyText(ModifyEvent e) {
							changeButtonVisibility();
						}
					});
					encryptedMessage = new Text(actionGroup_1, SWT.MULTI | SWT.WRAP | SWT.BORDER);
					encryptedMessage.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 33));
					
					createSpacer(actionGroup_1);
					createSpacer(actionGroup_1);
					
					label = new Label (actionGroup_1, SWT.NONE);
					label.setText("Empf\u00e4nger:");
					label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
					createSpacer(actionGroup_1);
					
					messageRecipient = new Combo(actionGroup_1, SWT.READ_ONLY);
					GridData gd_recp = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
					gd_recp.heightHint = 20;
					messageRecipient.setLayoutData(gd_recp);
					addReceipientsToCombo(tabfolder);
//					messageRecipient.select(0);
					messageRecipient.addSelectionListener(new SelectionListener() {
						
						@Override
						public void widgetSelected(SelectionEvent e) {
							changeButtonVisibility();
							fillRecipientKeys();
						}
						
						@Override
						public void widgetDefaultSelected(SelectionEvent e) {}
					});
					
					sendMessage = new Button(actionGroup_1, SWT.PUSH);
					sendMessage.setText("Nachricht senden");
					sendMessage.setEnabled(false);
					sendMessage.addSelectionListener(new SelectionAdapter() {
						@Override
						public void widgetSelected(final SelectionEvent e) {
							encryptedMessage.setText("nun wird die nachricht gesendet");
						}
					});
					sendMessage.setLayoutData(new GridData(SWT.RIGHT, SWT.RIGHT, true, false, 1, 1));
					
					label = new Label (actionGroup_1, SWT.NONE);
					label.setText("Schl\u00fcssel ausw\u00e4hlen:");
					GridData gd_lbl_rk = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
					gd_lbl_rk.heightHint = 20;
					label.setLayoutData(gd_lbl_rk);
					createSpacer(actionGroup_1);
					
					recipientKeys = new Combo(actionGroup_1, SWT.READ_ONLY);
					recipientKeys.add("testkey");
					recipientKeys.addSelectionListener(new SelectionListener() {
						
						@Override
						public void widgetSelected(SelectionEvent e) {
							changeButtonVisibility();
						}
						
						@Override
						public void widgetDefaultSelected(SelectionEvent e) {}
					});
					GridData gd_rk = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
					gd_rk.heightHint = 20;
					recipientKeys.setLayoutData(gd_rk);
					createSpacer(actionGroup_1);
					
					createSpacer(actionGroup_1);
					createSpacer(actionGroup_1);
					
					encryptMessage = new Button(actionGroup_1, SWT.PUSH);
					encryptMessage.setText("Nachricht verschl\u00fcsseln");
					encryptMessage.setEnabled(false);
					encryptMessage.addSelectionListener(new SelectionAdapter() {
						@Override
						public void widgetSelected(final SelectionEvent e) {
							encryptedMessage.setText("nun wird die nachricht verschl\u00fcsselt");
							sendMessage.setEnabled(true);
						}
					});
					encryptMessage.setLayoutData(new GridData(SWT.LEFT, SWT.LEFT, true, false, 1, 1));
					createSpacer(actionGroup_1);
					
					actionGroup_1.redraw();
					actionGroup_1.layout();
				}
				generalGroup.redraw();
				generalGroup.layout();
				forerunner = 1;
			}
		});
		enc_and_send.setText("Nachricht verschl\u00fcsseln und senden");
		enc_and_send.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		receive_and_decrypt = new Button(composite, SWT.PUSH);
		receive_and_decrypt.addSelectionListener(new SelectionAdapter() {
			@Override //Button 2
			public void widgetSelected(SelectionEvent e) {
				txtExplain.setText("Für den Entschlüsselungsvorgang werden die Parameter N und d benötigt. Mehr Informationen zu den einzelnen Parametern finden Sie in der Registerkarte „Meine Schlüssel“  in der „Schlüsselverwaltung“.\n\nVorgehensweise:\n1) Wählen Sie eine beliebige Nachricht aus dem Nachrichtenspeicher aus.\n\n2) Wählen Sie einen Ihrer privaten Schlüssel aus und geben Sie das Passwort dieses Schlüssels ein.\n\n3) Klicken Sie auf „Entschlüsseln“, um die Nachricht zu entschlüsseln. Wenn der passende private Schlüssel gewählt wurde, wird das Nachrichten-Chiffrat erfolgreich entschlüsselt. Falls ein falscher privater Schlüssel gewählt wurde, kann die Nachricht nicht korrekt entschlüsselt werden!\n\n4) Durch den Button \"Nachricht löschen\" kann die verschlüsselte Nachricht aus dem Nachrichtenspeicher gelöscht werden.");
				if (forerunner != 2){
					actionGroup_1.dispose();
					actionGroup_3.dispose();
					actionGroup_4.dispose();
					
					createActionGroup2();
					
					initActions2 = new Label(actionGroup_2, SWT.NONE);
					initActions2.setText("Nachricht ausw\u00e4hlen: ");
					GridData chMsg = new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1);
					chMsg.heightHint = 20;
					initActions2.setLayoutData(chMsg);
					createSpacer(actionGroup_2);	

					subjectChoose = new Combo(actionGroup_2, SWT.READ_ONLY);
					subjectChoose.add("asdf");	
					GridData gd_combo = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
					gd_combo.heightHint = 20;
					subjectChoose.setLayoutData(gd_combo);
					subjectChoose.addSelectionListener(new SelectionListener() {
						
						@Override
						public void widgetSelected(SelectionEvent e) {
							changeButtonVisibilityTab2();
						}
						
						@Override
						public void widgetDefaultSelected(SelectionEvent e) {}
					});
					
					createSpacer(actionGroup_2);
					
					label = new Label (actionGroup_2, SWT.NONE);
					label.setText("Verschl\u00fcsselte Nachricht:");
					GridData gd_message = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
					gd_message.heightHint = 20;
					label.setLayoutData(gd_message);
					
					label = new Label (actionGroup_2, SWT.NONE);
					label.setText("Entschl\u00fcsselte Nachricht:");
					label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
					
					encryptedMessage_Tab2= new Text(actionGroup_2, SWT.MULTI | SWT.WRAP | SWT.BORDER);
					encryptedMessage_Tab2.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 33));
					encryptedMessage_Tab2.addModifyListener(new ModifyListener() {
						
						@Override
						public void modifyText(ModifyEvent e) {
							changeButtonVisibilityTab2();
						}
					});
					decryptedMessage = new Text(actionGroup_2, SWT.MULTI | SWT.WRAP| SWT.BORDER);
					decryptedMessage.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 33));
					
					createSpacer(actionGroup_2);
					createSpacer(actionGroup_2);
					
					label = new Label (actionGroup_2, SWT.NONE);
					label.setText("Schl\u00fcssel ausw\u00e4hlen:");
					label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
					createSpacer(actionGroup_2);
					
					decryptionKey = new Combo(actionGroup_2, SWT.READ_ONLY);
					decryptionKey.add("blabla");
					decryptionKey.addSelectionListener(new SelectionListener() {
						
						@Override
						public void widgetSelected(SelectionEvent e) {
							changeButtonVisibilityTab2();
						}
						
						@Override
						public void widgetDefaultSelected(SelectionEvent e) {}
					});
					GridData gd_dk = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
					gd_dk.heightHint = 20;
					decryptionKey.setLayoutData(gd_dk);
					deleteMessage = new Button(actionGroup_2, SWT.PUSH);
					deleteMessage.setText("Nachricht l\u00f6schen");
					deleteMessage.setEnabled(false);
					deleteMessage.addSelectionListener(new SelectionAdapter() {
						@Override
						public void widgetSelected(final SelectionEvent e) {
							decryptedMessage.setText("nun wird die nachricht gel\u00f6scht");
						}
					});
					deleteMessage.setLayoutData(new GridData(SWT.RIGHT, SWT.RIGHT, true, false, 1, 1));
					
					label = new Label (actionGroup_2, SWT.NONE);
					label.setText("Passwort eingeben:");
					GridData gd_pw = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
					gd_pw.heightHint = 20;
					label.setLayoutData(gd_pw);
					createSpacer(actionGroup_2);
					
					pwPrivKey = new Text(actionGroup_2, SWT.BORDER);
					pwPrivKey.addModifyListener(new ModifyListener() {
						
						@Override
						public void modifyText(ModifyEvent e) {
							changeButtonVisibilityTab2();	
						}
					});
					
					GridData gd_key = new GridData(SWT.LEFT, SWT.LEFT, true, false, 1, 1);
					gd_key.widthHint = 200;
					gd_key.heightHint = 20;
					pwPrivKey.setLayoutData(gd_key);
					createSpacer(actionGroup_2);
					
					createSpacer(actionGroup_2);
					createSpacer(actionGroup_2);
					
					decryptMessage = new Button(actionGroup_2, SWT.PUSH);
					decryptMessage.setText("Nachricht entschl\u00fcsseln");
					decryptMessage.setEnabled(false);
					decryptMessage.addSelectionListener(new SelectionAdapter() {
						@Override
						public void widgetSelected(final SelectionEvent e) {
							decryptedMessage.setText("nun wird die nachricht verschl\u00fcsselt");
							deleteMessage.setEnabled(true);
						}
					});
					decryptMessage.setLayoutData(new GridData(SWT.LEFT, SWT.LEFT, true, false, 1, 1));
					createSpacer(actionGroup_2);
					
					
					generalGroup.redraw();
					generalGroup.layout();
					forerunner = 2;
				}
				
			}
		});
		receive_and_decrypt.setText("Nachricht empfangen und entschl\u00fcsseln");
		receive_and_decrypt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		keymanagement = new Button(composite, SWT.PUSH);
		keymanagement.addSelectionListener(new SelectionAdapter() {
			@Override //Button 3
			public void widgetSelected(SelectionEvent e) {
				txtExplain.setText("Hier kann ein Schlüssel mit ausgewählten Parametern erstellt werden. Der RSA-Algorithmus ist in zwei Varianten implementiert:\na) Für den klassischen RSA-Algorithmus werden hierfür zwei verschiedene Primzahlen (p und q) benötigt. Diese können entweder aus der Liste ausgewählt oder eingegeben werden.Weiters muss ein Exponent e angegeben werden, der entweder aus der Liste ausgesucht, durch den entsprechenden Button zufällig gewählt, oder eingegeben werden kann, sofern die Parameter p und q zulässig sind.\n\nb) Beim „multi-primen RSA“ muss zuerst die Anzahl der verwendeten Primzahlen (zwischen 3 und 5) festgelegt werden. Die Primzahlen und der Exponent können auch hier entweder aus der Liste gewählt, vom Programm generiert oder selbst eingegeben werden.\n\nAm Ende der Schlüsselerzeugung muss bei beiden Varianten ein Passwort für den geheimen Schlüssel festgelegt werden.");
				if (forerunner != 3){
					actionGroup_1.dispose();
					actionGroup_2.dispose();
					actionGroup_4.dispose();
					
					bi_rsaE = null;
					bi_rsaP = null;
					bi_rsaQ = null;
					
					createActionGroup3();
					
					tf_keyMgmt = new TabFolder(actionGroup_3, SWT.NONE);
					tf_keyMgmt.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
					tf_keyMgmt.addSelectionListener(new SelectionListener() {
						
						@Override
						public void widgetSelected(SelectionEvent e) {
							if (tf_keyMgmt.getSelectionIndex() == 0){
								txtExplain.setText("Hier kann ein Schlüssel mit ausgewählten Parametern erstellt werden. Der RSA-Algorithmus ist in zwei Varianten implementiert:\na) Für den klassischen RSA-Algorithmus werden hierfür zwei verschiedene Primzahlen (p und q) benötigt. Diese können entweder aus der Liste ausgewählt oder eingegeben werden.Weiters muss ein Exponent e angegeben werden, der entweder aus der Liste ausgesucht, durch den entsprechenden Button zufällig gewählt, oder eingegeben werden kann, sofern die Parameter p und q zulässig sind.\n\nb) Beim „multi-primen RSA“ muss zuerst die Anzahl der verwendeten Primzahlen (zwischen 3 und 5) festgelegt werden. Die Primzahlen und der Exponent können auch hier entweder aus der Liste gewählt, vom Programm generiert oder selbst eingegeben werden.\n\nAm Ende der Schlüsselerzeugung muss bei beiden Varianten ein Passwort für den geheimen Schlüssel festgelegt werden.");
							}
							if (tf_keyMgmt.getSelectionIndex() == 1){
								txtExplain.setText("In dieser Registerkarte können Schlüssel mit aktuell verwendeten Schlüssellängen erstellt werden. Es muss nur die gewünschte Schlüssellänge bzw. die Anzahl der Primzahlen ausgewählt werden.\n\nAm Ende der Schlüsselerzeugung muss bei beiden Varianten (zwei oder mehrere Primzahlen) ein Passwort für den geheimen Schlüssel festgelegt werden.");
							}
							if (tf_keyMgmt.getSelectionIndex() == 2){
								txtExplain.setText("Ein Schlüsselpaar besteht aus einem „privaten Schlüssel“ und einem „öffentlichen Schlüssel“:\na) Parameter eines privaten Schlüssels (N, d):\n-Der Modulus N ist sowohl Teil des öffentlichen wie des privaten Schlüssels.\n-Der private Exponent d muss geheim gehalten werden. Er ergab sich aus der Berechnung e^(-1) modulo phi(N).\n\nb) Parameter eines öffentlichen Schlüssels (N, e):\n-Der Modulus N ist sowohl Teil des öffentlichen wie des privaten Schlüssels.\n-Der öffentliche Exponent e (er befindet sich üblicherweise im Bereich von 17 – 65537).\n\nBeim „multi-primen“ RSA-Verfahren können mehr als zwei Primzahlen gewählt werden. In diesem Plugin ist die Anzahl auf drei bis fünf Primzahlen beschränkt. Theoretisch ist die Anzahl nach oben offen.");
							}
						}
						
						@Override
						public void widgetDefaultSelected(SelectionEvent e) {}
					});
					
					//Tab "New Key"
					keyMgmt_1 = new TabItem(tf_keyMgmt, SWT.NONE);
					keyMgmt_1.setText("Neuen Schl\u00fcssel erstellen");
					tab1 = new Composite(tf_keyMgmt, SWT.NONE);
					tab1.setLayout(new GridLayout(1, false));
					keyMgmt_1.setControl(tab1);
					
					
					init_tab1 = new Label(tab1, SWT.NONE);
					init_tab1.setText("W\u00e4hlen Sie 2 verschiedene Primzahlen p und q, sowie einen Exponenten e:");
					init_tab1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
					
					errorLabel_1 = new Label(tab1, SWT.NONE);
					errorLabel_1.setFont(FontService.getNormalBoldFont());
					errorLabel_1.setForeground(SWTResourceManager.getColor(SWT.COLOR_RED));
					errorLabel_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
					
					radio_RSA = new Button(tab1, SWT.RADIO);
					radio_RSA.setText("RSA");
					radio_RSA.setSelection(true);
					radio_RSA.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
					radio_RSA.addSelectionListener(new SelectionListener() {
						
						@Override
						public void widgetSelected(SelectionEvent e) {
							changeRSAVisibility();
							init_tab1.setText("W\u00e4hlen Sie 2 verschiedene Primzahlen p und q, sowie einen Exponenten e:");
						}
						
						@Override
						public void widgetDefaultSelected(SelectionEvent e) {}
					});
					
					//composite for the labels and dropdows
					rsaComposite = new Composite(tab1, SWT.NONE);
					rsaComposite.setLayout(new GridLayout(10, false));
					
					Label rsaP = new Label(rsaComposite, SWT.NONE);
					rsaP.setText("p:");
					GridData gd_rsa_p = new GridData(SWT.RIGHT, SWT.RIGHT, true, true, 1, 1);
					gd_rsa_p.heightHint = 20;
					rsaP.setLayoutData(gd_rsa_p);
					
					combo_rsaP = new Combo(rsaComposite, SWT.NONE);
					fillPrimesTo(combo_rsaP);
					combo_rsaP.addModifyListener(ml);
					combo_rsaP.addVerifyListener(VL);
					combo_rsaP.addSelectionListener(new SelectionListener() {
						
						@Override
						public void widgetSelected(SelectionEvent e) {
					    	//check if p and q are primes and e is valid
							bi_rsaP = new BigInteger(combo_rsaP.getItem(combo_rsaP.getSelectionIndex()));
					        checkParameter();
							if (qIsPrime&&pIsPrime){
								fillE();
							}
						}
						
						@Override
						public void widgetDefaultSelected(SelectionEvent e) {}
					});
					combo_rsaP.addKeyListener(new KeyListener() {
						
						@Override
						public void keyReleased(KeyEvent e) {
							if (combo_rsaP.getText().length() >0){
								bi_rsaP = new BigInteger(combo_rsaP.getText());
								checkParameter();
								if (qIsPrime&&pIsPrime){
									fillE();
								}
							}
						}
						
						@Override
						public void keyPressed(KeyEvent e) {}
					});
					GridData gd_combo_rsaP = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
					gd_combo_rsaP.heightHint = 20;
					combo_rsaP.setLayoutData(gd_combo_rsaP);
					
					Label rsaQ = new Label(rsaComposite, SWT.NONE);
					rsaQ.setText("q:");
					GridData gd_rsa_q = new GridData(SWT.RIGHT, SWT.RIGHT, true, true, 1, 1);
					gd_rsa_q.heightHint = 20;
					rsaQ.setLayoutData(gd_rsa_q);
					
					combo_rsaQ = new Combo(rsaComposite, SWT.NONE);
					fillPrimesTo(combo_rsaQ);
					combo_rsaQ.addModifyListener(ml);
					combo_rsaQ.addVerifyListener(VL);
					GridData gd_combo_rsaQ = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
					gd_combo_rsaQ.heightHint = 20;
					combo_rsaQ.setLayoutData(gd_combo_rsaQ);
					combo_rsaQ.addSelectionListener(new SelectionListener() {
						
						@Override
						public void widgetSelected(SelectionEvent e) {
					    	//check if p and q are primes and e is valid
							bi_rsaQ = new BigInteger(combo_rsaQ.getItem(combo_rsaQ.getSelectionIndex()));
					        checkParameter();
							if (qIsPrime&&pIsPrime){
								fillE();
							}
						}
						
						@Override
						public void widgetDefaultSelected(SelectionEvent e) {}
					});
					combo_rsaQ.addKeyListener(new KeyListener() {
						@Override
						public void keyReleased(KeyEvent e) {
							if (combo_rsaQ.getText().length() >0){
								bi_rsaQ = new BigInteger(combo_rsaQ.getText());
								checkParameter();
								if (qIsPrime&&pIsPrime){
									fillE();
								}
							}
						}
						
						@Override
						public void keyPressed(KeyEvent e) {}
					});
					
					for(int i = 0; i < 6; i++){
						createSpacer(rsaComposite);
					}
					
					Label rsaE = new Label(rsaComposite, SWT.NONE);
					rsaE.setText("e:");
					GridData gd_rsa_e = new GridData(SWT.RIGHT, SWT.RIGHT, true, true, 1, 1);
					gd_rsa_e.heightHint = 20;
					rsaE.setLayoutData(gd_rsa_e);
					
					combo_rsaE = new Combo(rsaComposite, SWT.NONE);
					GridData gd_combo_rsaE = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
					gd_combo_rsaE.heightHint = 20;
					gd_combo_rsaE.widthHint = 100;
					combo_rsaE.addVerifyListener(VL);
					combo_rsaE.setLayoutData(gd_combo_rsaE);
					combo_rsaE.addKeyListener(new KeyListener() {
						
						@Override
						public void keyReleased(KeyEvent e) {
							if (combo_rsaE.getText().length() >0){
								bi_rsaE = new BigInteger(combo_rsaE.getText());
								checkParameter();
							}
						}
						
						@Override
						public void keyPressed(KeyEvent e) {
						}
					});
					combo_rsaE.addSelectionListener(new SelectionListener() {
						
						@Override
						public void widgetSelected(SelectionEvent e) {
							bi_rsaE = new BigInteger(combo_rsaE.getItem(combo_rsaE.getSelectionIndex()));
					        checkParameter();
						}
						
						@Override
						public void widgetDefaultSelected(SelectionEvent e) {}
					});
					
					pickRandomE = new Button(rsaComposite, SWT.PUSH);
			        pickRandomE.setText("zuf\u00e4lliges 'E' w\u00e4hlen");
			        pickRandomE.setEnabled(false);
			        pickRandomE.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 2, 1));
			        pickRandomE.addSelectionListener(new SelectionListener() {

			            public void widgetSelected(SelectionEvent e) {
			            	bi_rsaE = possibleEs.get((int)(Math.random()*possibleEs.size()));
			            	combo_rsaE.setText(bi_rsaE.toString());
			            	eIsValid = true;
			            	errorLabel_1.setText("");
			            }

			            public void widgetDefaultSelected(SelectionEvent e) {}
			        });
					
					for(int i = 0; i < 3; i++){
						createSpacer(rsaComposite);
					}
					
					
					radio_ExtRSA = new Button(tab1, SWT.RADIO);
					radio_ExtRSA.addSelectionListener(new SelectionListener() {
						
						@Override
						public void widgetSelected(SelectionEvent e) {
							changeRSAVisibility();
							init_tab1.setText("W\u00e4hlen Sie 3 verschiedene Primzahlen p, q, r und einen Exponenten e:");
						}
						
						@Override
						public void widgetDefaultSelected(SelectionEvent e) {}
					});
					radio_ExtRSA.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, true, 1, 1));
					radio_ExtRSA.setText("Multi-primer RSA");
					
					//composite for the labels and dropdows
					rsaExMainComposite = new Composite(tab1, SWT.NONE);
					rsaExMainComposite.setLayout(new GridLayout(1, false));
					
					rsaExComposite1 = new Composite(rsaExMainComposite, SWT.NONE);
					rsaExComposite1.setLayout(new GridLayout(10, true));
					
					Label rsa_ex_Anz = new Label(rsaExComposite1, SWT.NONE);
					rsa_ex_Anz.setText("Anzahl der Primzahlen (3-5):");
					GridData gd_rsa_ex_Anz = new GridData(SWT.RIGHT, SWT.RIGHT, true, true, 1, 1);
					gd_rsa_ex_Anz.heightHint = 20;
					rsaP.setLayoutData(gd_rsa_ex_Anz);
					
					numberOfPrimesExRSA = new Combo(rsaExComposite1, SWT.READ_ONLY);
					GridData gd_combo_numberP = new GridData(SWT.LEFT, SWT.LEFT, true, false, 1, 1);
					gd_combo_numberP.heightHint = 20;
					gd_combo_numberP.widthHint = 50;
					numberOfPrimesExRSA.setLayoutData(gd_combo_numberP);
					numberOfPrimesExRSA.setEnabled(false);
					numberOfPrimesExRSA.addSelectionListener(new SelectionListener() {
						
						@Override
						public void widgetSelected(SelectionEvent e) {
							int value = Integer.parseInt(numberOfPrimesExRSA.getItem(numberOfPrimesExRSA.getSelectionIndex()));
							switch (value){
								case 3: combo_ExrsaS.setVisible(false);
										combo_ExrsaT.setVisible(false);
										rsa_ex_S.setVisible(false);
										rsa_ex_T.setVisible(false);
										init_tab1.setText("W\u00e4hlen Sie 3 verschiedene Primzahlen p, q, r und einen Exponenten e:");
										break;
										
								case 4: combo_ExrsaS.setVisible(true);
										combo_ExrsaT.setVisible(false);
										rsa_ex_S.setVisible(true);
										rsa_ex_T.setVisible(false);
										init_tab1.setText("W\u00e4hlen Sie 4 verschiedene Primzahlen p, q, r, s und einen Exponenten e:");
										break;
								
								case 5: combo_ExrsaS.setVisible(true);
										combo_ExrsaT.setVisible(true);
										rsa_ex_S.setVisible(true);
										rsa_ex_T.setVisible(true);
										init_tab1.setText("W\u00e4hlen Sie 5 verschiedene Primzahlen p, q, r, s, t und einen Exponenten e:");
										break;
										
								default:password1.setText("");
										password2.setText("");
										createKey.setEnabled(false);
										combo_ExrsaE.removeAll();
										pickRandomExtE.setEnabled(false);
										break;
							}
						}
						
						@Override
						public void widgetDefaultSelected(SelectionEvent e) {}
					});
					
					for (int i = 3; i < 6; i++){
						numberOfPrimesExRSA.add(""+i);
					}
					numberOfPrimesExRSA.select(0);
					for (int i = 0; i < 8; i++){
						createSpacer(rsaExComposite1);
					}
					
					rsaExComposite2 = new Composite(rsaExMainComposite, SWT.NONE);
					rsaExComposite2.setLayout(new GridLayout(10, false));
					
					//P
					Label rsa_ex_P = new Label(rsaExComposite2, SWT.NONE);
					rsa_ex_P.setText("p:");
					GridData gd_rsa_exP = new GridData(SWT.RIGHT, SWT.RIGHT, true, true, 1, 1);
					gd_rsa_exP.heightHint = 20;
					rsa_ex_P.setLayoutData(gd_rsa_exP);
					combo_ExrsaP = new Combo(rsaExComposite2, SWT.NONE);
					GridData gd_combo_ExrsaP = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
					gd_combo_ExrsaP.heightHint = 20;
					gd_combo_ExrsaP.widthHint = 100;
					combo_ExrsaP.setLayoutData(gd_combo_ExrsaP);
					combo_ExrsaP.addVerifyListener(VL);
					combo_ExrsaP.setEnabled(false);
					fillPrimesTo(combo_ExrsaP);
					combo_ExrsaP.addSelectionListener(new SelectionListener() {
						@Override
						public void widgetSelected(SelectionEvent e) {
							bi_ExtrsaP = new BigInteger(combo_ExrsaP.getItem(combo_ExrsaP.getSelectionIndex()));
					        checkParameter();
						}
						
						@Override
						public void widgetDefaultSelected(SelectionEvent e) {}
					});
					combo_ExrsaP.addKeyListener(new KeyListener() {
						@Override
						public void keyReleased(KeyEvent e) {
							if (combo_ExrsaP.getText().length() >0){
								bi_ExtrsaP = new BigInteger(combo_ExrsaP.getText());
								checkParameter();
							}
						}
						
						@Override
						public void keyPressed(KeyEvent e) {}
					});
					
					//Q
					Label rsa_ex_Q = new Label(rsaExComposite2, SWT.NONE);
					rsa_ex_Q.setText("q:");
					GridData gd_rsa_exQ = new GridData(SWT.RIGHT, SWT.RIGHT, true, true, 1, 1);
					gd_rsa_exQ.heightHint = 20;
					rsa_ex_Q.setLayoutData(gd_rsa_exQ);
					combo_ExrsaQ = new Combo(rsaExComposite2, SWT.NONE);
					GridData gd_combo_ExrsaQ = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
					gd_combo_ExrsaQ.heightHint = 20;
					gd_combo_ExrsaQ.widthHint = 100;
					combo_ExrsaQ.setLayoutData(gd_combo_ExrsaQ);
					combo_ExrsaQ.addVerifyListener(VL);
					combo_ExrsaQ.setEnabled(false);
					fillPrimesTo(combo_ExrsaQ);
					combo_ExrsaQ.addSelectionListener(new SelectionListener() {
						@Override
						public void widgetSelected(SelectionEvent e) {
							bi_ExtrsaQ = new BigInteger(combo_ExrsaQ.getItem(combo_ExrsaQ.getSelectionIndex()));
					        checkParameter();
						}
						
						@Override
						public void widgetDefaultSelected(SelectionEvent e) {}
					});
					combo_ExrsaQ.addKeyListener(new KeyListener() {
						@Override
						public void keyReleased(KeyEvent e) {
							if (combo_ExrsaQ.getText().length() >0){
								bi_ExtrsaQ = new BigInteger(combo_ExrsaQ.getText());
								checkParameter();
							}
						}
						
						@Override
						public void keyPressed(KeyEvent e) {}
					});
					
					//R
					Label rsa_ex_R = new Label(rsaExComposite2, SWT.NONE);
					rsa_ex_R.setText("r:");
					GridData gd_rsa_exR = new GridData(SWT.RIGHT, SWT.RIGHT, true, true, 1, 1);
					gd_rsa_exR.heightHint = 20;
					rsa_ex_R.setLayoutData(gd_rsa_exR);
					combo_ExrsaR = new Combo(rsaExComposite2, SWT.NONE);
					GridData gd_combo_ExrsaR = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
					gd_combo_ExrsaR.heightHint = 20;
					gd_combo_ExrsaR.widthHint = 100;
					combo_ExrsaR.setLayoutData(gd_combo_ExrsaR);
					combo_ExrsaR.addVerifyListener(VL);
					combo_ExrsaR.setEnabled(false);
					fillPrimesTo(combo_ExrsaR);
					combo_ExrsaR.addSelectionListener(new SelectionListener() {
						@Override
						public void widgetSelected(SelectionEvent e) {
							bi_ExtrsaR = new BigInteger(combo_ExrsaR.getItem(combo_ExrsaR.getSelectionIndex()));
					        checkParameter();
						}
						
						@Override
						public void widgetDefaultSelected(SelectionEvent e) {}
					});
					combo_ExrsaR.addKeyListener(new KeyListener() {
						@Override
						public void keyReleased(KeyEvent e) {
							if (combo_ExrsaR.getText().length() >0){
								bi_ExtrsaR = new BigInteger(combo_ExrsaR.getText());
								checkParameter();
							}
						}
						
						@Override
						public void keyPressed(KeyEvent e) {}
					});
					
					//S
					rsa_ex_S = new Label(rsaExComposite2, SWT.NONE);
					rsa_ex_S.setText("s:");
					GridData gd_rsa_exS = new GridData(SWT.RIGHT, SWT.RIGHT, true, true, 1, 1);
					gd_rsa_exS.heightHint = 20;
					rsa_ex_S.setLayoutData(gd_rsa_exS);
					rsa_ex_S.setVisible(false);
					combo_ExrsaS = new Combo(rsaExComposite2, SWT.NONE);
					GridData gd_combo_ExrsaS = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
					gd_combo_ExrsaS.heightHint = 20;
					gd_combo_ExrsaS.widthHint = 100;
					combo_ExrsaS.setLayoutData(gd_combo_ExrsaS);
					combo_ExrsaS.addVerifyListener(VL);
					combo_ExrsaS.setEnabled(false);
					combo_ExrsaS.setVisible(false);
					fillPrimesTo(combo_ExrsaS);
					combo_ExrsaS.addSelectionListener(new SelectionListener() {
						@Override
						public void widgetSelected(SelectionEvent e) {
							bi_ExtrsaS = new BigInteger(combo_ExrsaS.getItem(combo_ExrsaS.getSelectionIndex()));
					        checkParameter();
						}
						
						@Override
						public void widgetDefaultSelected(SelectionEvent e) {}
					});
					combo_ExrsaS.addKeyListener(new KeyListener() {
						@Override
						public void keyReleased(KeyEvent e) {
							if (combo_ExrsaS.getText().length() >0){
								bi_ExtrsaS = new BigInteger(combo_ExrsaS.getText());
								checkParameter();
							}
						}
						
						@Override
						public void keyPressed(KeyEvent e) {}
					});
					
					//T
					rsa_ex_T = new Label(rsaExComposite2, SWT.NONE);
					rsa_ex_T.setText("t:");
					GridData gd_rsa_exT = new GridData(SWT.RIGHT, SWT.RIGHT, true, true, 1, 1);
					gd_rsa_exT.heightHint = 20;
					rsa_ex_T.setLayoutData(gd_rsa_exT);
					rsa_ex_T.setVisible(false);
					combo_ExrsaT = new Combo(rsaExComposite2, SWT.NONE);
					GridData gd_combo_ExrsaT = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
					gd_combo_ExrsaT.heightHint = 20;
					gd_combo_ExrsaT.widthHint = 100;
					combo_ExrsaT.setLayoutData(gd_combo_ExrsaT);
					combo_ExrsaT.addVerifyListener(VL);
					combo_ExrsaT.setEnabled(false);
					combo_ExrsaT.setVisible(false);
					fillPrimesTo(combo_ExrsaT);
					combo_ExrsaT.addSelectionListener(new SelectionListener() {
						@Override
						public void widgetSelected(SelectionEvent e) {
							bi_ExtrsaT = new BigInteger(combo_ExrsaT.getItem(combo_ExrsaT.getSelectionIndex()));
					        checkParameter();
						}
						
						@Override
						public void widgetDefaultSelected(SelectionEvent e) {}
					});
					combo_ExrsaT.addKeyListener(new KeyListener() {
						@Override
						public void keyReleased(KeyEvent e) {
							if (combo_ExrsaT.getText().length() >0){
								bi_ExtrsaT = new BigInteger(combo_ExrsaT.getText());
								checkParameter();
							}
						}
						
						@Override
						public void keyPressed(KeyEvent e) {}
					});
					
					//E
					Label rsa_ex_E = new Label(rsaExComposite2, SWT.NONE);
					rsa_ex_E.setText("e:");
					GridData gd_rsa_exE = new GridData(SWT.RIGHT, SWT.RIGHT, true, true, 1, 1);
					gd_rsa_exE.heightHint = 20;
					rsa_ex_E.setLayoutData(gd_rsa_exE);
					combo_ExrsaE = new Combo(rsaExComposite2, SWT.NONE);
					GridData gd_combo_ExrsaE = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
					gd_combo_ExrsaE.heightHint = 20;
					gd_combo_ExrsaE.widthHint = 100;
					combo_ExrsaE.setLayoutData(gd_combo_ExrsaE);
					combo_ExrsaE.addVerifyListener(VL);
					combo_ExrsaE.setEnabled(false);
					combo_ExrsaE.addKeyListener(new KeyListener() {
						@Override
						public void keyReleased(KeyEvent e) {
							if (combo_ExrsaE.getText().length() >0){
								bi_ExtrsaE = new BigInteger(combo_ExrsaE.getText());
								checkParameter();
							}
						}
						
						@Override
						public void keyPressed(KeyEvent e) {
						}
					});
					combo_ExrsaE.addSelectionListener(new SelectionListener() {
						@Override
						public void widgetSelected(SelectionEvent e) {
							bi_ExtrsaE = new BigInteger(combo_ExrsaE.getItem(combo_ExrsaE.getSelectionIndex()));
					        checkParameter();
						}
						
						@Override
						public void widgetDefaultSelected(SelectionEvent e) {}
					});
					
					
					pickRandomExtE = new Button(rsaExComposite2, SWT.PUSH);
					pickRandomExtE.setText("zuf\u00e4lliges 'E' w\u00e4hlen");
					pickRandomExtE.setEnabled(false);
					pickRandomExtE.setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, true, 2, 1));
			        pickRandomExtE.addSelectionListener(new SelectionListener() {

			            public void widgetSelected(SelectionEvent e) {
			            	bi_ExtrsaE = possibleEs.get((int)(Math.random()*possibleEs.size()));
			            	combo_ExrsaE.setText(bi_ExtrsaE.toString());
			            	eIsValid = true;
			            	errorLabel_1.setText("");
			            }

			            public void widgetDefaultSelected(SelectionEvent e) {}
			        });
					
					new Label(rsaExMainComposite, SWT.SEPARATOR | SWT.HORIZONTAL).setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));					
					
					rsaExComposite3 = new Composite(rsaExMainComposite, SWT.NONE);
					rsaExComposite3.setLayout(new GridLayout(5, true));
					
					for (int i = 0; i < 3; i++){
						createSpacer(rsaExComposite3);
					}
					
					//enter password
					Label rsa_password = new Label(rsaExComposite3, SWT.NONE);
					rsa_password.setText("Passwort eingeben:");
					GridData gd_rsa_pw = new GridData(SWT.RIGHT, SWT.RIGHT, true, true, 1, 1);
					gd_rsa_pw.heightHint = 20;
					rsa_password.setLayoutData(gd_rsa_pw);
					password1 = new Text(rsaExComposite3, SWT.PASSWORD| SWT.BORDER);
					GridData gd_combo_pw1 = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
					gd_combo_pw1.heightHint = 20;
					gd_combo_pw1.widthHint = 100;
					password1.setLayoutData(gd_combo_pw1);
					password1.addModifyListener(new ModifyListener() {
						
						@Override
						public void modifyText(ModifyEvent e) {
							pw1 = password1.getText();
							checkPasswords();
						}
					});
					
					for (int i = 0; i < 3; i++){
						createSpacer(rsaExComposite3);
					}
					//enter password again
					Label rsa_password2 = new Label(rsaExComposite3, SWT.NONE);
					rsa_password2.setText("Passwort wiederholen:");
					GridData gd_rsa_pw2 = new GridData(SWT.RIGHT, SWT.RIGHT, true, true, 1, 1);
					gd_rsa_pw2.heightHint = 20;
					rsa_password2.setLayoutData(gd_rsa_pw2);
					password2 = new Text(rsaExComposite3, SWT.PASSWORD| SWT.BORDER);
					GridData gd_combo_pw2 = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
					gd_combo_pw2.heightHint = 20;
					gd_combo_pw2.widthHint = 100;
					password2.setLayoutData(gd_combo_pw2);
					password2.addModifyListener(new ModifyListener() {
						
						@Override
						public void modifyText(ModifyEvent e) {
							pw2 = password2.getText();
							checkPasswords();
						}
					});
					
					for (int i = 0; i < 4; i++){
						createSpacer(rsaExComposite3);
					}
					
					createKey = new Button(rsaExComposite3, SWT.PUSH);
					createKey.setText("Schl\u00fcssel erstellen");
					createKey.setEnabled(false);
					GridData gd_createKey = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
					gd_createKey.heightHint = 20;
					gd_createKey.widthHint = 100;
					createKey.setLayoutData(gd_createKey);
					createKey.addSelectionListener(new SelectionListener() {
						
						@Override
						public void widgetSelected(SelectionEvent e) {
							if (radio_RSA.getSelection()){
								txtExplain.setText("nun wird ein klassicher RSA key erstellt. Parameter: P"+bi_rsaP+" Q: "+bi_rsaQ+" E:"+bi_rsaE+" pw: "+pw1);
							}else{
								txtExplain.setText("nun wird ein mp-RSA key erstellt. Parameter: P"+bi_ExtrsaP+" Q: "+bi_ExtrsaQ+" R: "+bi_ExtrsaR+"E:"+bi_ExtrsaE+" pw: "+pw1);
							}
						}
						
						@Override
						public void widgetDefaultSelected(SelectionEvent e) {}
					});
					
					
					//Tab "New Key (extended)"
					keyMgmt_2 = new TabItem(tf_keyMgmt, SWT.NONE);
					keyMgmt_2.setText("Neuen Schl\u00fcssel erstellen (erweitert)");
					tab2 = new Composite(tf_keyMgmt, SWT.NONE);
					tab2.setLayout(new GridLayout(1, false));
					keyMgmt_2.setControl(tab2);
					
					Label lbl_init_tab2 = new Label(tab2, SWT.NONE);
					lbl_init_tab2.setText("Hier können Sie neue Schl\u00fcssel mit g\u00e4ngigen Schl\u00fcsselgr\u00f6ßen erstellen:");
					GridData gd_init_tab2 = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
					gd_init_tab2.heightHint = 20;
					lbl_init_tab2.setLayoutData(gd_init_tab2);
					
					
					radio_RSA_tab2 = new Button(tab2, SWT.RADIO);
					radio_RSA_tab2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
					radio_RSA_tab2.setText("RSA");
					radio_RSA_tab2.setSelection(true);
					radio_RSA_tab2.addSelectionListener(new SelectionListener() {
						
						@Override
						public void widgetSelected(SelectionEvent e) {
							changeRSAVisibility_Tab2();
						}
						
						@Override
						public void widgetDefaultSelected(SelectionEvent e) {}
					});
					
					Composite rsaComp = new Composite(tab2, SWT.NONE);
					rsaComp.setLayout(new GridLayout(2, false));
					
					
					Label rsa_label = new Label(rsaComp, SWT.NONE);
					rsa_label.setText("Schl\u00fcssell\u00e4nge w\u00e4hlen:");
					GridData gd_rsa_lab = new GridData(SWT.RIGHT, SWT.RIGHT, true, true, 1, 1);
					gd_rsa_lab.heightHint = 20;
					rsa_label.setLayoutData(gd_rsa_lab);
					
					rsa_length = new Combo(rsaComp, SWT.READ_ONLY);
					rsa_length.add("1024");
					rsa_length.add("2048");
					GridData gd_rsa_length_comb = new GridData(SWT.LEFT, SWT.LEFT, true, true, 1, 1);
					gd_rsa_length_comb.heightHint = 20;
					rsa_length.select(0);
					rsa_length.setLayoutData(gd_rsa_length_comb);
					
					for (int i = 0; i < 4; i++){
						createSpacer(rsaComp);
					}
					
					
					radio_ExtRSA_tab2 = new Button(tab2, SWT.RADIO);
					radio_ExtRSA_tab2.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, false, false, 1, 1));
					radio_ExtRSA_tab2.setText("Multi-primer RSA");
					radio_ExtRSA_tab2.addSelectionListener(new SelectionListener() {
						
						@Override
						public void widgetSelected(SelectionEvent e) {
							changeRSAVisibility_Tab2();
						}
						
						@Override
						public void widgetDefaultSelected(SelectionEvent e) {}
					});
					
					Composite rsaExtComp = new Composite(tab2, SWT.NONE);
					rsaExtComp.setLayout(new GridLayout(2, false));
					
					
					Label rsa_extLabel = new Label(rsaExtComp, SWT.NONE);
					rsa_extLabel.setText("Anzahl der Primzahlen (3-5):");
					GridData gd_rsa_extlab = new GridData(SWT.RIGHT, SWT.RIGHT, true, true, 1, 1);
					gd_rsa_extlab.heightHint = 20;
					rsa_extLabel.setLayoutData(gd_rsa_extlab);
					
					extRsa_numberPrimes_tab2 = new Combo(rsaExtComp, SWT.READ_ONLY);
					GridData gd_combo_ExtnumberP = new GridData(SWT.LEFT, SWT.LEFT, true, false, 1, 1);
					gd_combo_ExtnumberP.heightHint = 20;
					gd_combo_ExtnumberP.widthHint = 50;
					extRsa_numberPrimes_tab2.setLayoutData(gd_combo_ExtnumberP);
					extRsa_numberPrimes_tab2.setEnabled(false);
					for (int i = 3; i < 6; i++){
						extRsa_numberPrimes_tab2.add(""+i);
					}
					extRsa_numberPrimes_tab2.select(0);
					
					Label rsa_extlength = new Label(rsaExtComp, SWT.NONE);
					rsa_extlength.setText("Schl\u00fcssell\u00e4nge w\u00e4hlen:");
					GridData gd_rsa_extlength = new GridData(SWT.LEFT, SWT.LEFT, true, true, 1, 1);
					gd_rsa_extlength.heightHint = 20;
					rsa_extlength.setLayoutData(gd_rsa_extlength);
					
					extRsa_length = new Combo(rsaExtComp, SWT.READ_ONLY);
					extRsa_length.add("1024");
					extRsa_length.add("2048");
					GridData gd_Extrsa_length_comb = new GridData(SWT.LEFT, SWT.LEFT, true, true, 1, 1);
					gd_Extrsa_length_comb.heightHint = 20;
					extRsa_length.select(0);
					extRsa_length.setLayoutData(gd_Extrsa_length_comb);
					extRsa_length.setEnabled(false);
					
					new Label(tab2, SWT.NONE).setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
					new Label(tab2, SWT.NONE).setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
					
					new Label(tab2, SWT.SEPARATOR | SWT.HORIZONTAL).setLayoutData(new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1));
					
					
					rsaExComposite3 = new Composite(tab2, SWT.NONE);
					rsaExComposite3.setLayout(new GridLayout(5, true));
					
					for (int i = 0; i < 3; i++){
						createSpacer(rsaExComposite3);
					}
					
					//enter password
					Label rsa_password_2 = new Label(rsaExComposite3, SWT.NONE);
					rsa_password_2.setText("Passwort eingeben:");
					GridData gd_rsa_pw_2 = new GridData(SWT.RIGHT, SWT.RIGHT, true, true, 1, 1);
					gd_rsa_pw_2.heightHint = 20;
					rsa_password_2.setLayoutData(gd_rsa_pw_2);
					ext_password1 = new Text(rsaExComposite3, SWT.PASSWORD| SWT.BORDER);
					GridData gd_combo_pw1_2 = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
					gd_combo_pw1_2.heightHint = 20;
					gd_combo_pw1_2.widthHint = 100;
					ext_password1.setLayoutData(gd_combo_pw1_2);
					ext_password1.addModifyListener(new ModifyListener() {
						
						@Override
						public void modifyText(ModifyEvent e) {
							pw1_Ext = ext_password1.getText();
							checkPasswords();
						}
					});
					
					
					
					for (int i = 0; i < 3; i++){
						createSpacer(rsaExComposite3);
					}
					//enter password again
					Label rsa_password2_2 = new Label(rsaExComposite3, SWT.NONE);
					rsa_password2_2.setText("Passwort wiederholen:");
					GridData gd_rsa_pw2_2 = new GridData(SWT.RIGHT, SWT.RIGHT, true, true, 1, 1);
					gd_rsa_pw2_2.heightHint = 20;
					rsa_password2_2.setLayoutData(gd_rsa_pw2_2);
					ext_password2 = new Text(rsaExComposite3, SWT.PASSWORD| SWT.BORDER);
					GridData gd_combo_pw2_2 = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
					gd_combo_pw2_2.heightHint = 20;
					gd_combo_pw2_2.widthHint = 100;
					ext_password2.setLayoutData(gd_combo_pw2_2);
					ext_password2.addModifyListener(new ModifyListener() {
						
						@Override
						public void modifyText(ModifyEvent e) {
							pw2_Ext = ext_password2.getText();
							checkPasswords();
						}
					});
					
					for (int i = 0; i < 4; i++){
						createSpacer(rsaExComposite3);
					}
					
					createKey_Tab2 = new Button(rsaExComposite3, SWT.PUSH);
					createKey_Tab2.setText("Schl\u00fcssel erstellen");
					GridData gd_createKey_2 = new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1);
					gd_createKey_2.heightHint = 20;
					gd_createKey_2.widthHint = 100;
					createKey_Tab2.setLayoutData(gd_createKey_2);
					createKey_Tab2.setEnabled(false);
					createKey_Tab2.addSelectionListener(new SelectionListener() {
						
						@Override
						public void widgetSelected(SelectionEvent e) {
							if (radio_ExtRSA_tab2.getSelection()){
								txtExplain.setText("Nun wird großer multiprimer RSA-key erstellt");	
							}else{
								txtExplain.setText("Nun wird großer klassischer RSA-key erstellt");	
							}
						}
						
						@Override
						public void widgetDefaultSelected(SelectionEvent e) {}
					});
					
					
					//Tab "My Keys"
					keyMgmt_3 = new TabItem(tf_keyMgmt, SWT.NONE);
					keyMgmt_3.setText("Meine Schl\u00fcssel");
					tab3 = new Composite(tf_keyMgmt, SWT.NONE);;
					tab3.setLayout(new GridLayout(1, false));
					keyMgmt_3.setControl(tab3);
					
					Label lbl_init_tab3 = new Label(tab3, SWT.WRAP);
					lbl_init_tab3.setText("Hier k\u00f6nnen Sie sich Ihre privaten Schl\u00fcsselpaare und die \u00f6ffentlichen Schl\u00fcssel aller Mitspieler ansehen. \n\nHinweis: Um einen Ihrer privaten Schl\u00fcssel anzuzeigen, m\u00fcssen Sie Ihr Passwort eingeben. Die Anzeige \u00f6ffentlicher Schl\u00fcssel erfordert keine Passwort-Eingabe.");
					lbl_init_tab3.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 3));
					
					
					Composite myKeyData = new Composite(tab3, SWT.NONE);
					myKeyData.setLayout(new GridLayout(4, false));
					myKeyData.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
					
					//combo "select key"
					Label lbl_selectKey = new Label(myKeyData, SWT.WRAP);
					lbl_selectKey.setText("Schl\u00fcssel ausw\u00e4hlen:");
					GridData gd_lbl_selKey = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
					gd_lbl_selKey.heightHint = 20;
					lbl_selectKey.setLayoutData(gd_lbl_selKey);
					selectedKey_Keydata = new Combo(myKeyData, SWT.READ_ONLY);
					GridData gd_selKey = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
					gd_selKey.heightHint = 20;
					gd_selKey.widthHint = 200;
					selectedKey_Keydata.setLayoutData(gd_selKey);
					
					for (int i = 0; i < 2; i++){
						createSpacer(myKeyData);
					}
					
					
					//textfield "enter password"
					Label lbl_enterPW = new Label(myKeyData, SWT.NONE);
					lbl_enterPW.setText("Passwort eingeben:");
					GridData gd_enterPW = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
					gd_enterPW.heightHint = 20;
					lbl_enterPW.setLayoutData(gd_enterPW);
					password_keydata = new Text(myKeyData, SWT.PASSWORD|SWT.BORDER);
					GridData gd_pw = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
					gd_pw.heightHint = 20;
					gd_pw.widthHint = 200;
					password_keydata.setLayoutData(gd_pw);
					
					//button "show keydata"
					showKeydata = new Button(myKeyData, SWT.PUSH);
					GridData gd_showKeydata = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
					gd_showKeydata.heightHint = 20;
					gd_showKeydata.widthHint = 100;
					showKeydata.setLayoutData(gd_showKeydata);
					showKeydata.setText("Schl\u00fcsseldaten anzeigen");
					showKeydata.addSelectionListener(new SelectionListener() {
						
						@Override
						public void widgetSelected(SelectionEvent e) {
							keyData.removeAll();
							for (int i = 0; i < 10; i++){
								TableItem ti = new TableItem(keyData, SWT.NONE);
								ti.setText(new String[]{"wert in s1: "+i, "wert in S2: "+i});
							}
							
						}
						
						@Override
						public void widgetDefaultSelected(SelectionEvent e) {}
					});
					
					createSpacer(myKeyData);
					
					new Label(tab3, SWT.SEPARATOR | SWT.HORIZONTAL).setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
					
					keyData = new Table(tab3, SWT.BORDER|SWT.FULL_SELECTION);
					GridData gd_table = new GridData(SWT.FILL, SWT.FILL, false, false, 1, 1);
					gd_table.heightHint = 100;
					keyData.setLayoutData(gd_table);
					keyData.setHeaderVisible(true);
					keyData.setLinesVisible(true);
					
					
					column_parameter = new TableColumn(keyData, SWT.NONE);
					column_parameter.setWidth(100);
					column_parameter.setText("Parameter");
					
					column_value = new TableColumn(keyData, SWT.NONE);
					column_value.setWidth(500);
					column_value.setText("Wert");
					
					TableItem ti = new TableItem(keyData, SWT.NONE);
					ti.setText(new String[]{"wert in s1", "wert in S2"});
					
					generalGroup.redraw();
					generalGroup.layout();
					forerunner = 3;
				}
			}
		});
		keymanagement.setText("Schl\u00fcsselverwaltung");
		keymanagement.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		attackPublicKey = new Button(composite, SWT.PUSH);
		attackPublicKey.addSelectionListener(new SelectionAdapter() {
			@Override //Button 4
			public void widgetSelected(SelectionEvent e) {
				
				
			}
		});
		attackPublicKey.setText("\u00d6ffentlichen Schl\u00fcssel angreifen");
		attackPublicKey.setLayoutData(new GridData(SWT.FILL, SWT.CENTER, true, false, 1, 1));
		
		//add spacer to the buttons on the left (for further actions :) )
		for (int i = 0; i < 15; i++){
			createSpacer(composite);
		}
		
		createActionGroup1();
		
		createActionGroup2();
		group_2.exclude = true;
		actionGroup_2.setVisible(false);
		
		createActionGroup3();
		group_3.exclude = true;
		actionGroup_3.setVisible(false);
		
		actionGroup_4 = new Group(generalGroup, SWT.NONE);
		actionGroup_4.setText("Aktionsfenster 4");
		actionLayout_4 = new GridLayout(1, false);
		actionGroup_4.setLayout(actionLayout_4);
		group_4 = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		group_4.exclude = true;
		actionGroup_4.setLayoutData(group_4);
		actionGroup_4.setVisible(false);
		
	}
	
	private void fillRecipientKeys(){
//		Vector<String> rec = iMgr.getPublicKeys(this.identityName);
		rec = iMgr.getPublicKeys(messageRecipient.getText());
		recipientKeys.setItems(rec.keySet().toArray(new String[rec.size()]));
		recipientKeys.select(0);
	}
	
	
	private void changeButtonVisibilityTab2(){
		if ((subjectChoose.getSelectionIndex() != -1) &&(encryptedMessage_Tab2.getText().length() >1 ) && (decryptionKey.getSelectionIndex() != -1) && (pwPrivKey.getText().length() > 0)){
			decryptMessage.setEnabled(true);
		}
	}
	
	
	private void changeButtonVisibility(){
		if ((clearMessage.getText().length() > 0) && (recipientKeys.getSelectionIndex() != -1) && (messageRecipient.getSelectionIndex() != -1)){
			encryptMessage.setEnabled(true);
		}
	}
	
	private void checkPasswords(){
		if (pw1 != null && pw2 != null){
			if (pw1.equals(pw2) && eIsValid){
				createKey.setEnabled(true);
			}else{
				createKey.setEnabled(false);
			}
			System.out.println("[DEBUG]"+pw1.length()+ "-"+ pw2.length()+"+++++ "+eIsValid);
		}
		if(pw1_Ext != null && pw2_Ext != null){
			if (pw1_Ext.equals(pw2_Ext)){
				createKey_Tab2.setEnabled(true);
			}else{
				createKey_Tab2.setEnabled(false);
			}
		}
		
	}
	
	private void changeRSAVisibility_Tab2(){
		if (radio_RSA_tab2.getSelection()){
			//Radiobutton "RSA" is activated
			rsa_length.setEnabled(true);
			extRsa_length.setEnabled(false);
			extRsa_numberPrimes_tab2.setEnabled(false);
		}else{
			//Radiobutton "Multi-prime RSA" is activated
			rsa_length.setEnabled(false);
			extRsa_length.setEnabled(true);
			extRsa_numberPrimes_tab2.setEnabled(true);
		}
	}
	
	
	private void changeRSAVisibility(){
		if (radio_RSA.getSelection()){
			//Radiobutton "RSA" is activated
			
			pickRandomExtE.setEnabled(false);
			eIsValid = false;
			
			combo_rsaE.setEnabled(true);
			combo_rsaP.setEnabled(true);
			combo_rsaQ.setEnabled(true);
			combo_ExrsaP.setEnabled(false);
			combo_ExrsaQ.setEnabled(false);
			combo_ExrsaR.setEnabled(false);
			combo_ExrsaS.setEnabled(false);
			combo_ExrsaT.setEnabled(false);
			combo_ExrsaE.setEnabled(false);
			
			combo_ExrsaS.setVisible(false);
			combo_ExrsaT.setVisible(false);
			rsa_ex_S.setVisible(false);
			rsa_ex_T.setVisible(false);
			
			numberOfPrimesExRSA.select(0);
			numberOfPrimesExRSA.setEnabled(false);
			
			errorLabel_1.setText("");
			
		}else{
			//Radiobutton "Multi-prime RSA" is activated
			pickRandomE.setEnabled(false);
			eIsValid = false;
			
			numberOfPrimesExRSA.setEnabled(true);
			numberOfPrimesExRSA.select(0);
			combo_rsaE.setEnabled(false);
			combo_rsaP.setEnabled(false);
			combo_rsaQ.setEnabled(false);
			combo_ExrsaP.setEnabled(true);
			combo_ExrsaQ.setEnabled(true);
			combo_ExrsaR.setEnabled(true);
			combo_ExrsaS.setEnabled(true);
			combo_ExrsaT.setEnabled(true);
			combo_ExrsaE.setEnabled(true);
			
			combo_ExrsaS.setVisible(false);
			combo_ExrsaT.setVisible(false);
			rsa_ex_S.setVisible(false);
			rsa_ex_T.setVisible(false);
		}
		password1.setText("");
		password2.setText("");
		createKey.setEnabled(false);
		
		errorLabel_1.setText("");
		
		resetRSAValues();
	}
	private void resetRSAValues(){
		bi_rsaE = null;
		bi_rsaP = null;
		bi_rsaQ = null;
		bi_ExtrsaP = null;
		bi_ExtrsaQ = null;
		bi_ExtrsaR = null;
		bi_ExtrsaS = null;
		bi_ExtrsaT = null;
		bi_ExtrsaE = null;
		
		if(combo_rsaE != null){
			combo_rsaE.setText("");
		}
		if(combo_rsaP != null){
			combo_rsaP.setText("");
		}
		if(combo_rsaQ != null){
			combo_rsaQ.setText("");
		}
		
		if(combo_ExrsaP != null){
			combo_ExrsaP.setText("");
		}
		if(combo_ExrsaQ != null){
			combo_ExrsaQ.setText("");
		}
		if(combo_ExrsaR != null){
			combo_ExrsaR.setText("");
		}
		if(combo_ExrsaS != null){
			combo_ExrsaS.setText("");
		}
		if(combo_ExrsaT != null){
			combo_ExrsaT.setText("");
		}
		if(combo_ExrsaE != null){
			combo_ExrsaE.setText("");
		}
	}
	
	private void addReceipientsToCombo(TabFolder tabfolder){
		Vector<String> recipients = new Vector<String>();
		if (messageRecipient != null){
			messageRecipient.removeAll();
		}
		
		//fill in possible recipients (split is necessary because we store Alice e.g. as "Alice Whitehat" and we only display the first name)
		
		for (String s : iMgr.getContacts()){
			String[]split_name = s.split(" ");
//			if (!split_name[0].equals(this.identityName) && !recipients.contains(split_name[0])){
			if (!recipients.contains(split_name[0])){
				recipients.add(split_name[0]);
				System.out.println("[DEBUG] filling receipients... adding: "+split_name[0]);
			}
		}
		
		messageRecipient.setItems(recipients.toArray(new String[recipients.size()]));
	}
	
    /**
     * enters all primes into the given combo item.
     *
     * @param combo the list from which a prime can be selected
     */
    private void fillPrimesTo(final Combo combo) {
        for (Integer i : LOW_PRIMES) {
            combo.add(i.toString());
        }
    }
	
	private void createActionGroup1(){
		actionGroup_1 = new Group(generalGroup, SWT.NONE);
		actionGroup_1.setText("Aktionsfenster");
		actionLayout_1 = new GridLayout(2, true);
		actionLayout_1.horizontalSpacing = 80;

		actionGroup_1.setLayout(actionLayout_1);
		group_1 = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		group_1.exclude = false;
		actionGroup_1.setLayoutData(group_1);
		initActions = new Label(actionGroup_1, SWT.NONE);
		initActions.setText("Bitte w\u00e4hlen Sie eine Aktion.");
		actionGroup_1.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));
	}
	private void createActionGroup2(){
		actionGroup_2 = new Group(generalGroup, SWT.NONE);
		actionGroup_2.setText("Aktionsfenster");
		actionLayout_2 = new GridLayout(2, true);
		actionLayout_2.horizontalSpacing = 80;
		actionGroup_2.setLayout(actionLayout_2);
		group_2 = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		actionGroup_2.setLayoutData(group_2);
	}
	private void createActionGroup3(){
		actionGroup_3 = new Group(generalGroup, SWT.NONE);
		actionGroup_3.setText("Aktionsfenster");
		actionLayout_3 = new GridLayout(1, false);
		actionLayout_3.horizontalSpacing = 80;
		actionGroup_3.setLayout(actionLayout_3);
		group_3 = new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1);
		actionGroup_3.setLayoutData(group_3);
	}
	
	private Label createSpacer(final Composite location){
		Label spacer = new Label(location, SWT.NONE);
		spacer.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 1, 1));
//		spacer.setText("     ");
		return spacer;
	}

	public final String getIdentityName() {
		return identityName;
	}

	public final void setIdentityName(String identityName) {
		this.identityName = identityName;
	}


	public final String getForename() {
		return forename;
	}

	public final void setForename(final String forename) {
		this.forename = forename;
	}

	public final String getSurname() {
		return surname;
	}

	public final void setSurname(final String surname) {
		this.surname = surname;
	}

	public final String getOrganisation() {
		return organisation;
	}

	public final void setOrganisation(final String organisation) {
		this.organisation = organisation;
	}

	public final String getRegion() {
		return region;
	}

	public final void setRegion(final String region) {
		this.region = region;
	}

	public int getId() {
		return id;
	}

	@Override 
	protected void checkSubclass() { 
	}
	
	/**
     * fills the list of possible e values for selection.
     * "0" represents the classic RSA and "1" for multi-prime
     */
    private void fillElist(final int algorithm) {
        new Thread() {

            /**
             * Runnable for accessing the user interface
             *
             * @author Michael Gaber
             */
            final class KeyRunnable implements Runnable {

                /** itemcount that is added directly to the list */
                private static final int TRIGGERLENGTH = 20000;

                /** list of items to add */
                private final String[] newList;

                /**
                 * Constructor sets the list
                 *
                 * @param list the list of items to add to combo_rsaE
                 * @param intermediate whether this result is an intermediate one, so enable the transfer-button
                 */
                private KeyRunnable(String[] list) {
                    this.newList = list;
                }

                public void run() {
                    if (newList.length <= TRIGGERLENGTH) {
                    	if(algorithm == 0){
                    		combo_rsaE.setItems(newList);
                        	if(bi_rsaE != null){
                        		//really important!!
                        		combo_rsaE.setText(bi_rsaE.toString());
                        	}
                    	}else{
                    		combo_ExrsaE.setItems(newList);
                        	if(bi_ExtrsaE != null){
                        		//really important!!
                        		combo_ExrsaE.setText(bi_ExtrsaE.toString());
                        	}
                    	}
                    	
                    } 
                }
            }

            @Override
            public void run() {
                Set<BigInteger> tempEList = new TreeSet<BigInteger>();
                BigInteger ih;
                if (algorithm == 0){
	                for (int i = 2; i < bi_rsaPhi.intValue(); i++) {
	                    ih = new BigInteger("" + i); //$NON-NLS-1$
	                    if (bi_rsaPhi.gcd(ih).equals(ONE)) {
	                        if (tempEList.size() == KeyRunnable.TRIGGERLENGTH) {
	                        	fillToE(tempEList);
	                        }
	                        tempEList.add(ih);
	                    }
	                }
                }else{
                	for (int i = 2; i < bi_ExtrsaPhi.intValue(); i++) {
	                    ih = new BigInteger("" + i); //$NON-NLS-1$
	                    if (bi_ExtrsaPhi.gcd(ih).equals(ONE)) {
	                        if (tempEList.size() == KeyRunnable.TRIGGERLENGTH) {
	                        	fillToE(tempEList);
	                        }
	                        tempEList.add(ih);
	                    }
	                }
                }
                
                //now, the tempEList contains all elements
                fillToE(tempEList);
                
                possibleEs = new Vector<BigInteger>(tempEList);

            }
            /**
             * transfers the given list of items to an array of strings, creates a new Keyrunnable and starts it using
             * the {@link Display#asyncExec(Runnable)} Method.
             *
             * @param list the list of items to set
             */
            private void fillToE(Set<BigInteger> list) {
                List<String> newList = new LinkedList<String>();
                for (BigInteger integer : list) {
                    newList.add(integer.toString());
                }
                Display.getDefault().asyncExec(new KeyRunnable(newList.toArray(new String[newList.size()])));
            }
        }.start();
    }
    private void checkParameter(){
    	pIsPrime = false;
    	qIsPrime = false;
    	rIsPrime = false;
    	sIsPrime = false;
    	tIsPrime = false;
    	eIsValid = false;
    	
    	if (radio_RSA.getSelection()){
	    	if(bi_rsaP!=null){
	    		if (!bi_rsaP.equals(Constants.MINUS_ONE) && !Lib.isPrime(bi_rsaP)) {
		        	errorLabel_1.setText("Achtung: 'P' ist keine Primzahl!");
		        	combo_rsaE.removeAll();
		        	pIsPrime = false;
		        	init = false;
		        	pickRandomE.setEnabled(false); 
		        	eIsValid = false;
		        }else{
	        		errorLabel_1.setText("");
	        		pIsPrime = true;
		        }
	    	}
	        if (bi_rsaQ!=null){
		        if (!bi_rsaQ.equals(Constants.MINUS_ONE) && !Lib.isPrime(bi_rsaQ)) {
		        	errorLabel_1.setText("Achtung: 'Q' ist keine Primzahl!");
		        	combo_rsaE.removeAll();
		        	qIsPrime = false;
		        	init = false;
		        	pickRandomE.setEnabled(false);
		        	eIsValid = false;
		        }else{
		        	errorLabel_1.setText("");
		        	qIsPrime = true;
		        }
	        }
	        //show only the error-message again, because the message will be overwritten in the code above
	        if (qIsPrime && !pIsPrime && bi_rsaP != null){
	        	errorLabel_1.setText("Achtung: 'P' ist keine Primzahl!");
	        }
	        
	        if(pIsPrime && qIsPrime){
		        if (!bi_rsaP.equals(bi_rsaQ)) {
		        	bi_rsaN = bi_rsaP.multiply(bi_rsaQ);                    	
		        	bi_rsaPhi = Lib.calcPhi(bi_rsaP, bi_rsaQ);
//		        	errorLabel_1.setText("");
		        	pickRandomE.setEnabled(true); 
		        	if (init){
		        		combo_rsaE.removeAll();
			            fillElist(0);
			            init = false;
		        	}
		            if(bi_rsaE != null){
		            	if (!possibleEs.contains(bi_rsaE)) {
		    	        	errorLabel_1.setText("Achtung: 'E' ist kein passender Exponent!");
		    	        	eIsValid = false;
		    	        }else{
		    	        	errorLabel_1.setText("");
		    	        	eIsValid = true;
		    	        }
		            }
	
		        } else {
		        	bi_rsaN = Constants.MINUS_ONE;
		        	bi_rsaPhi = Constants.MINUS_ONE;
		        	combo_rsaE.removeAll();
		        	errorLabel_1.setText("Achtung: 'P' und 'Q' d\u00fcrfen nicht den selben Wert haben!");
		        	pickRandomE.setEnabled(false);
		        }
	        }
    	}else{
    		//check the multi-prime RSA parameters
    		bi_ExtrsaN = Constants.MINUS_ONE;
    		bi_ExtrsaPhi = Constants.MINUS_ONE;
    		
    		if(bi_ExtrsaP!=null){
	    		if (!bi_ExtrsaP.equals(Constants.MINUS_ONE) && !Lib.isPrime(bi_ExtrsaP)) {
		        	errorLabel_1.setText("Achtung: 'P' ist keine Primzahl!");
		        	combo_ExrsaE.removeAll();
		        	pIsPrime = false;
		        	init = false;
		        	pickRandomExtE.setEnabled(false); 
		        	eIsValid = false;
		        }else{
	        		errorLabel_1.setText("");
	        		pIsPrime = true;
		        }
	    	}
    		if(bi_ExtrsaQ!=null){
	    		if (!bi_ExtrsaQ.equals(Constants.MINUS_ONE) && !Lib.isPrime(bi_ExtrsaQ)) {
		        	errorLabel_1.setText("Achtung: 'Q' ist keine Primzahl!");
		        	combo_ExrsaE.removeAll();
		        	qIsPrime = false;
		        	init = false;
		        	pickRandomExtE.setEnabled(false); 
		        	eIsValid = false;
		        }else{
	        		errorLabel_1.setText("");
	        		qIsPrime = true;
		        }
	    	}
    		if(bi_ExtrsaR!=null){
	    		if (!bi_ExtrsaR.equals(Constants.MINUS_ONE) && !Lib.isPrime(bi_ExtrsaR)) {
		        	errorLabel_1.setText("Achtung: 'R' ist keine Primzahl!");
		        	combo_ExrsaE.removeAll();
		        	rIsPrime = false;
		        	init = false;
		        	pickRandomExtE.setEnabled(false); 
		        	eIsValid = false;
		        }else{
	        		errorLabel_1.setText("");
	        		rIsPrime = true;
		        }
	    	}
    		if(bi_ExtrsaS!=null){
	    		if (!bi_ExtrsaS.equals(Constants.MINUS_ONE) && !Lib.isPrime(bi_ExtrsaS)) {
		        	errorLabel_1.setText("Achtung: 'S' ist keine Primzahl!");
		        	combo_ExrsaE.removeAll();
		        	sIsPrime = false;
		        	init = false;
		        	pickRandomExtE.setEnabled(false); 
		        	eIsValid = false;
		        }else{
	        		errorLabel_1.setText("");
	        		sIsPrime = true;
		        }
	    	}
    		if(bi_ExtrsaT!=null){
	    		if (!bi_ExtrsaT.equals(Constants.MINUS_ONE) && !Lib.isPrime(bi_ExtrsaT)) {
		        	errorLabel_1.setText("Achtung: 'T' ist keine Primzahl!");
		        	combo_ExrsaE.removeAll();
		        	tIsPrime = false;
		        	init = false;
		        	pickRandomExtE.setEnabled(false); 
		        	eIsValid = false;
		        }else{
	        		errorLabel_1.setText("");
	        		tIsPrime = true;
		        }
	    	}
    		//check belated changes and show only the message.. the rest is done in the methods above
    		if (!pIsPrime && bi_ExtrsaP != null){
    			errorLabel_1.setText("Achtung: 'P' ist keine Primzahl....!");
    		}
    		if (!qIsPrime && bi_ExtrsaQ != null){
    			errorLabel_1.setText("Achtung: 'Q' ist keine Primzahl!");
    		}
    		if (!rIsPrime && bi_ExtrsaR != null){
    			errorLabel_1.setText("Achtung: 'R' ist keine Primzahl!");
    		}
    		if (!sIsPrime && bi_ExtrsaS != null){
    			errorLabel_1.setText("Achtung: 'S' ist keine Primzahl!");
    		}
    		if (!tIsPrime && bi_ExtrsaT != null){
    			errorLabel_1.setText("Achtung: 'T' ist keine Primzahl!");
    		}
    		
    		//check, if different primes are chosen
    		validCount = 0;
    		TreeSet<BigInteger> checkPrimes = new TreeSet<BigInteger>();
    		if (bi_ExtrsaP != null && pIsPrime){
    			checkPrimes.add(bi_ExtrsaP);
    			validCount++;
    		}else{
    			combo_ExrsaE.removeAll();
    		}
    		
    		if (bi_ExtrsaQ != null && qIsPrime){
    			if(checkPrimes.contains(bi_ExtrsaQ)){
    				errorLabel_1.setText("Achtung: Bitte verschiedene Primzahlen ausw\u00e4hlen!");
    				combo_ExrsaE.removeAll();
    			}else{
        			checkPrimes.add(bi_ExtrsaQ);
        			validCount++;
        		}
    		}
    		
    		if (bi_ExtrsaR != null && rIsPrime){
    			if(checkPrimes.contains(bi_ExtrsaR)){
    				errorLabel_1.setText("Achtung: Bitte verschiedene Primzahlen ausw\u00e4hlen!");
    				combo_ExrsaE.removeAll();
    			}else{
        			checkPrimes.add(bi_ExtrsaR);
        			validCount++;
        		}
    		}
    		
    		if (bi_ExtrsaS != null && sIsPrime){
    			if(checkPrimes.contains(bi_ExtrsaS)){
    				errorLabel_1.setText("Achtung: Bitte verschiedene Primzahlen ausw\u00e4hlen!");
    				combo_ExrsaE.removeAll();
    			}else{
        			checkPrimes.add(bi_ExtrsaS);
        			validCount++;
        		}
    		}
    		
    		if (bi_ExtrsaT != null && tIsPrime){
    			if(checkPrimes.contains(bi_ExtrsaT)){
    				errorLabel_1.setText("Achtung: Bitte verschiedene Primzahlen ausw\u00e4hlen!");
    				combo_ExrsaE.removeAll();
    			}else{
        			checkPrimes.add(bi_ExtrsaT);
        			validCount++;
        		}
    		}
    		//fill E
    		int selectednumberOfPrimes = Integer.parseInt(numberOfPrimesExRSA.getItem(numberOfPrimesExRSA.getSelectionIndex()));
    		
    		if (validCount == selectednumberOfPrimes){
    			fillE();
    			switch (selectednumberOfPrimes){
    				case 3: bi_ExtrsaN = bi_ExtrsaP.multiply(bi_ExtrsaQ).multiply(bi_ExtrsaR);
    						bi_ExtrsaPhi = (bi_ExtrsaP.subtract(ONE)).multiply(bi_ExtrsaQ.subtract(ONE)).multiply(bi_ExtrsaR.subtract(ONE));
    						break;
    						
    				case 4: bi_ExtrsaN = bi_ExtrsaP.multiply(bi_ExtrsaQ).multiply(bi_ExtrsaR).multiply(bi_ExtrsaS);
							bi_ExtrsaPhi = (bi_ExtrsaP.subtract(ONE)).multiply(bi_ExtrsaQ.subtract(ONE)).multiply(bi_ExtrsaR.subtract(ONE)).multiply(bi_ExtrsaR.subtract(ONE));
							break;
							
    				case 5: bi_ExtrsaN = bi_ExtrsaP.multiply(bi_ExtrsaQ).multiply(bi_ExtrsaR).multiply(bi_ExtrsaS).multiply(bi_ExtrsaT);
							bi_ExtrsaPhi = (bi_ExtrsaP.subtract(ONE)).multiply(bi_ExtrsaQ.subtract(ONE)).multiply(bi_ExtrsaR.subtract(ONE)).multiply(bi_ExtrsaR.subtract(ONE)).multiply(bi_ExtrsaT.subtract(ONE));
							break;
    			}
    			pickRandomExtE.setEnabled(true);
    			if (init){
	        		combo_ExrsaE.removeAll();
		            fillElist(1);
		            init = false;
	        	}
    			if(bi_ExtrsaE != null){
	            	if (!possibleEs.contains(bi_ExtrsaE)) {
	    	        	errorLabel_1.setText("Achtung: 'E' ist kein passender Exponent!");
	    	        	eIsValid = false;
	    	        }else{
	    	        	errorLabel_1.setText("");
	    	        	eIsValid = true;
	    	        }
	            }
    		}
    	}
    	
        
    }
    private void fillE(){
    	if(radio_RSA.getSelection()){
    		eIsValid = false;
        	password1.setText("");
        	password2.setText("");
        	createKey.setEnabled(false);
        	
	        if(pIsPrime && qIsPrime){
	        	combo_rsaE.removeAll();
		        if (!bi_rsaP.equals(bi_rsaQ)) {
		            fillElist(0);
		        } else {
		        	init = true;
		        }
	        }
    	}else{
    		combo_ExrsaE.removeAll();
    		
    		if (validCount == Integer.parseInt(numberOfPrimesExRSA.getItem(numberOfPrimesExRSA.getSelectionIndex()))){
    			fillElist(1);
    		}else{
    			init = true;
    		}
    	}
    }
}
