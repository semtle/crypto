package org.jcryptool.visual.aup.views;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Arrays;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.jcryptool.core.logging.utils.LogUtil;
import org.jcryptool.visual.aup.AndroidUnlockPatternPlugin;
import org.jcryptool.visual.aup.views.AupView.ApuState;

/**
 * 
 * @author Stefan Kraus <stefan.kraus05@gmail.com>
 * 
 * 
 */
public class Backend {

	private final static int arrayLengthStd = 10;

	private final static Color STANDARD = Display.getCurrent().getSystemColor(
			SWT.COLOR_WIDGET_BACKGROUND);
	private final static Color ROT = Display.getCurrent().getSystemColor(
			SWT.COLOR_RED);
	private final static Color GELB = Display.getCurrent().getSystemColor(
			SWT.COLOR_YELLOW);
	private final static Color GRUEN = Display.getCurrent().getSystemColor(
			SWT.COLOR_GREEN);
	private Color lineColor = Display.getCurrent().getSystemColor(
			SWT.COLOR_WIDGET_BACKGROUND);

//	private boolean isActive[] = new boolean[arrayLengthStd];
	private boolean isChangeable = false;
	private boolean first = true;
//	private boolean cancelShowed = false;
	private int modus;// 1=set;2=change;3=check
	private int length = 0;
	private int matrixSize = 3;	//size of the matrix; AUP 3x3
	private int tryCount = 0;
	private int[] order = new int[arrayLengthStd];
	private int[] ordersaved;
	private int[] ordertmp;
//	int[][] lines = new int[8][2];
	int[][] points = new int[8][4];

	private AupView visual;

	/**
	 * Initialize some variables
	 * 
	 * @param aupView
	 */
	public Backend(AupView aupView) {
		visual = aupView;
//		for (int i = 0; i < lines.length; i++) {
//			for (int j = 0; j < lines[i].length; j++) {
//				lines[i][j] = 0;
//			}
//		}
		for (int i = 0; i < points.length; i++) {
			for (int j = 0; j < points[i].length; j++) {
				points[i][j] = 0;
			}

		}
	}

	/**
	 * @return the points
	 */
	public int[][] getPoints() {
		return this.points;
	}

//	/**
//	 * @param points
//	 *            the points to set
//	 */
//	public void setPoints(int[][] points) {
//		this.points = points;
//	}

	/**
	 * adds the number to the array which should be checked.
	 * 
	 * @param btnNummer
	 *            number which should be added to the patter
	 */
	public void add(int btnNummer) {
		if (this.length > 9) {
			LogUtil.logError("pattern is longer then 9 -> impossible"); //$NON-NLS-1$
		} else if (length == 0) {
		//add starting point	
			order[length] = btnNummer + 1;
			length++;
		} else {
		//add point
			int px1 = (order[length - 1] - 1) % matrixSize;	//order: numbers from 1 to 9
			int py1 = (order[length - 1] - 1) / matrixSize; //order: numbers from 1 to 9
			int px2 = btnNummer % matrixSize; //btnNummer: numbers from 0 to 8
			int py2 = btnNummer / matrixSize; //btnNummer: numbers from 0 to 8
			int dx = px2 - px1;
			int dy = py2 - py1;
			float arc = (float)(Math.atan2(dy, dx) * 180 / Math.PI);
			
			if(Math.abs(dx) > 1 || Math.abs(dy) > 1){
			//check for intermediate points 
				//read in used points
				boolean used[] = new boolean[matrixSize*matrixSize];
				for(int i = 0; i < length; i++) {
					used[order[i]-1] = true;
				}
				int steps, intP;
				if(dx == 0) {
				//vertical line	
					steps = Math.abs(dy) + 1; //calculate the number of points on the line
					dy = (int)Math.signum(dy);
					for(int i = 0; i < steps; i++){
					//add all intermediate points
						intP = ((py1 + i * dy) * matrixSize + px1);
						if(!used[intP]) { //check if point is already used -> add if not
							visual.getCntrBtn()[order[length-1] - 1].setData("arc", arc);
							order[length] = intP + 1; //+1 because the buttons are numbered from 1 to 9
							length++;
						}
					}
				} else if (dy == 0) {
				//horizontal line
					steps = Math.abs(dx) + 1; //calculate the number of points on the line
					dx = (int)Math.signum(dx);
					for(int i = 0; i < steps; i++){
					//add all intermediate points
						intP = (py1 * matrixSize + (px1 + i * dx));
						if(!used[intP]) { //check if point is already used -> add if not
							visual.getCntrBtn()[order[length-1] - 1].setData("arc", arc);
							order[length] = intP + 1; //+1 because the buttons are numbered from 1 to 9
							length++;
						}
					}
				} else {
					steps = dx;
					int gcd;
					while((gcd = gcd(dx, dy)) != 1) {
					//eliminate all common divisors
						dx /= gcd;
						dy /= gcd;
					}
					steps = (steps / dx) + 1; //calculate the number of points on the line
					for(int i = 0; i < steps; i++){
					//add all intermediate points
						intP = ((py1 + i * dy) * matrixSize + (px1 + i * dx));
						if(!used[intP]) { //check if point is already used -> add if not
							visual.getCntrBtn()[order[length-1] - 1].setData("arc", arc);
							order[length] = intP + 1; //+1 because the buttons are numbered from 1 to 9
							length++;
						}
					}
				}
			} else {
			//no intermediate points; dx == 0, dy == +-1 || dx == +-1 , dy == 0 || dx == dy == 0
			//add the clicked point	
				visual.getCntrBtn()[order[length-1] - 1].setData("arc", arc);
				order[length] = btnNummer + 1;
				length++;
			}
		}
//		for(int a : order)
//			System.out.print(a + " ");
//		System.out.println();
	}
	
	/**
	 * Computes the greatest common divisor of a and b.
	 * <p>
	 * Enforce that neither a nor b is 0!
	 * 
	 * @param a != 0
	 * @param b != 0
	 * @return gcd(a, b)
	 */
    static int gcd(int a, int b) {
    	int x = Math.abs(a);
    	int y = Math.abs(b);
		while(x != y) {
		    if (x > y) x = x - y;
		    else y = y - x;
		}
		return x;
    }

//    /**
//	 * Computes the least common multiple of a and b.
//	 * <p>
//	 * Enforce that neither a nor b is 0!
//     * 
//     * @param a
//     * @param b
//     * @return lcm
//     */
//    static int lcm(int a, int b) {
////    	if(a == 0 || b == 0)
////    		return 0;
////    	else
//    		return a * (b / gcd(a,b)); // Klammerung vermeidet Überlauf! 
//    }

	public void btnMainClick(int btnNummer) {
		add(btnNummer);
		visual.getBtnCancel().setEnabled(true);
//		if (!isValid()) {
//			setColor(ROT);
//			visual.getBtnSave().setEnabled(false);
//			visual.getBtnSave().setBackground(STANDARD);
//			visual.setStatusText(Messages.Backend_InfoTextInvalid, ApuState.ERROR);
//		} else 
		if (!isGreatEnough()) {
			setColor(GELB);
			visual.setStatusText(Messages.Backend_TEXT_TO_SHORT, ApuState.WARNING);
		} else {
			setColor(GRUEN);
			visual.getBtnSave().setEnabled(true);
			visual.getBtnSave().setBackground(GRUEN);
			visual.setStatusText(Messages.Backend_TEXT_VALID, ApuState.INFO);
		}
		if (length > 1) {
//			for (int[] line : lines) {
//				if (line[0] == 0 && line[1] == 0) {
//					line[0] = order[length - 2];
//					line[1] = order[length - 1];
//					visual.getCenterbox().redraw();
//					break;
//				}
//			}
			visual.getCenterbox().redraw();
		}
	}

	/**
	 * Resets all state information! Use with great care.
	 */
	public void reset() {
			ordersaved = new int[10];
			for (int i = 0; i < ordersaved.length; i++) {
				ordersaved[i] = 0;
			}
			resetBtn();
			resetOrder();
			save();
			setModus(1);
	}

	public void btnSaveClick() {
		if (modus == 1) { // set
			setPattern();
		} else if (modus == 2) { // change
			if (!isChangeable) {
				if (checkPattern()) {
					isChangeable = true;
					visual.updateProgress();
					visual.getBtnSave().setEnabled(false);
					visual.getBtnSave().setBackground(STANDARD);
				}
			} else {
				setPattern();
			}

		} else if (modus == 3) { // check
			checkPattern();
			cancel();
		} else {
			visual.MsgBox(Messages.Backend_PopupErrorHeading,
					Messages.Backend_PopupErrorMessage, SWT.ICON_ERROR
							| SWT.OK);
		}
	}

	/**
	 * Checks if the inputed pattern is valid. If so it is in case of the <b>first input</b> temporary saved. 
	 * In case of a <b>matching confirmation input</b> it is saved and the mode is changed to <i>check</i>.
	 * In case of a <b>not matching confirmation input</b> the input is reseted and an information message is displayed in the status text.
	 */
	private void setPattern() {
//		if (isValid() && isGreatEnough()) {
		if (isGreatEnough()) {
			if (first) {
				first = false;
				ordertmp = new int[arrayLengthStd];
				for (int i = 0; i < order.length; i++) {
					ordertmp[i] = order[i];
				}
				visual.getBtnSave().setText(Messages.AndroidUnlockPattern_ButtonSaveText);
				visual.updateProgress();
				visual.setStatusText("", null); //$NON-NLS-1$
				cancel();
			} else if (Arrays.equals(ordertmp, order)) {
				saveOrder();
				cancel();
				setModus(3);
				visual.setStatusText(Messages.Backend_PopupSavedMessage, ApuState.INFO);
			} else {
				// MsgBox unequal pattern or Error
				btnCancelClick();
				visual.setStatusText(Messages.Backend_PopupNotSavedMessage, ApuState.ERROR);
			}
		} else {
			btnCancelClick();
			visual.MsgBox(Messages.Backend_PopupInvalidHeading,
					Messages.Backend_PopupInvalidMessage, SWT.ICON_ERROR
							| SWT.OK);
		}
	}

	/**
	 * Checks if entered pattern equals saved one. Counts also the failed check attempts.
	 */
	private boolean checkPattern() {
		if (Arrays.equals(order, ordersaved)) {
			visual.setStatusText(Messages.Backend_PopupValidMessage, ApuState.OK);
			resetBtn();
			resetOrder();
			tryCount = 0;
			return true;
		} else {
			tryCount++;
			visual.setStatusText(String.format(Messages.Backend_PopupWrongMessage, tryCount), ApuState.ERROR);
			resetBtn();
			resetOrder();
			
		}
		return false;
	}

	private void saveOrder() {
		if (order.length == arrayLengthStd
				&& ordersaved.length == arrayLengthStd) {
			for (int i = 0; i < order.length; i++) {
				ordersaved[i] = order[i];
			}
			save();
		} else {
			LogUtil.logError("Interal Error: order.length!=ordersaved.length!=10"); //$NON-NLS-1$
		}

	}
	
	/**
	 * Resets the user input and cleans up the Mainbutton area.
	 */
	public void cancel() {
		resetBtn();
		resetOrder();
	}
	
	
	/**
	 * Simulated click to 'Cancel' button.<br>
	 * Resets the user input, cleans up the Mainbutton area and deletes the status text + image.
	 */
	public void btnCancelClick() {
		cancel();
		//modusChanged();
		
//		switch(modus) {
//		case 1: {
//			if (first) visual.getTextFeld().setText(Messages.Mode_Set_1); 
//			else visual.getTextFeld().setText(Messages.Mode_Set_2);
//			break; }
//		case 2: {
//			if (!isChangeable) visual.getTextFeld().setText(Messages.Mode_Change_1);
//			else if (first) visual.getTextFeld().setText(Messages.Mode_Change_2);
//			else visual.getTextFeld().setText(Messages.Mode_Set_2);
//			break; }
//		case 3: visual.getTextFeld().setText(Messages.Mode_Check_1); break;
//		}
		
		//reset text only when order comes from user
//		if(!silent) visual.getStatusLabel().setText("");
		visual.setStatusText("", null); //$NON-NLS-1$
	}

	/**
	 * Checks if a pattern can be load from a savefile and sets the applicable
	 * mode
	 */
	public void init() {

		restore();
		if (ordersaved == null || ordersaved.length != 10 || ordersaved[0] == 0
				|| ordersaved[1] == 0 || ordersaved[2] == 0
				|| ordersaved[3] == 0) { // noch kein Pattern vorhanden, oder
											// falsch gespeichert
			ordersaved = new int[10];
			for (int i = 0; i < ordersaved.length; i++) {
				ordersaved[i] = 0;
			}
			setModus(1);
		} else {// pattern vorhanden. Modus wird auf check gesetzt
			setModus(3);
		}
	}

	/**
	 * check whether the pattern is long enough (>4)
	 * 
	 * @return
	 */
	private boolean isGreatEnough() {
		if (length < 4 || length > 9) {
			return false;
		}
		return true;
	}

//	/**
//	 * @deprecated It is no longer possible to input invalid pattern.
//	 * check whether the pattern is valid(without checking the length)
//	 * 
//	 * @return
//	 */
//	private boolean isValid() {
//		for (int i = 0; i < isActive.length; i++) {
//			isActive[i] = false;
//		}
//
//		if (order.length != 10) {
//			try {
//				throw new Exception("Internal Error: Wrong length of Array"); //$NON-NLS-1$
//			} catch (Exception e) {
//				LogUtil.logError(e);
//			}
//		}
//		if (!(order[9] == 0)) {
//			LogUtil.logError("Internal Error:last cypher != 0"); //$NON-NLS-1$
//			return false;
//		}

//		boolean isEnd = false;
//		for (int i = 0; i < order.length - 1; i++) {
//
//			switch (order[i]) {
//			case 0:
//				isEnd = true;
//				/*
//				 * // Startpunkt==Endpunkt 
//				 * if (i > 0 && order[i - 1] == order[0]) { return false; }
//				 * // weniger als 4 Punkte 
//				 * else if (i < 4) { return false; }
//				 */
//				break;
//			case 1:
//
//				if (isEnd || isActive[1] || (order[i + 1] == 3 && !isActive[2])
//						|| (order[i + 1] == 7 && !isActive[4])
//						|| (order[i + 1] == 9 && !isActive[5])) {
//					return false;
//				}
//
//				isActive[1] = true;
//				break;
//			case 2:
//				if (isEnd || isActive[2] || (order[i + 1] == 8 && !isActive[5])) {
//					return false;
//				}
//				isActive[2] = true;
//				break;
//			case 3:
//				if (isEnd || isActive[3] || (order[i + 1] == 1 && !isActive[2])
//						|| (order[i + 1] == 7 && !isActive[5])
//						|| (order[i + 1] == 9 && !isActive[6])) {
//					return false;
//				}
//				isActive[3] = true;
//				break;
//			case 4:
//				if (isEnd || isActive[4] || (order[i + 1] == 6 && !isActive[5])) {
//					return false;
//				}
//				isActive[4] = true;
//				break;
//			case 5:
//				if (isEnd || isActive[5]) {
//					return false;
//				}
//				isActive[5] = true;
//				break;
//			case 6:
//				if (isEnd || isActive[6] || (order[i + 1] == 4 && !isActive[5])) {
//					return false;
//				}
//				isActive[6] = true;
//				break;
//			case 7:
//				if (isEnd || isActive[7] || (order[i + 1] == 1 && !isActive[4])
//						|| (order[i + 1] == 3 && !isActive[5])
//						|| (order[i + 1] == 9 && !isActive[8])) {
//					return false;
//				}
//				isActive[7] = true;
//				break;
//			case 8:
//				if (isEnd || isActive[8] || (order[i + 1] == 2 && !isActive[5])) {
//					return false;
//				}
//				isActive[8] = true;
//				break;
//			case 9:
//				if (isEnd || isActive[9] || (order[i + 1] == 1 && !isActive[5])
//						|| (order[i + 1] == 3 && !isActive[6])
//						|| (order[i + 1] == 7 && !isActive[8])) {
//					return false;
//				}
//				isActive[9] = true;
//				break;
//			}
//
//		}
//
//		return true;
//	}
	
	/**
	 * Resets the GUI and progress information to the initial state of the current mode.
	 */
	private void modusChanged() {
		//cancel current operation
		cancel();
		
		//reset progress
		isChangeable = false;
		first = true;
		setColor(STANDARD);
		visual.getBtnSave().setEnabled(false);
		visual.getBtnSave().setBackground(STANDARD);
		
		switch(modus) {
			case 1:
			case 2: {
				visual.getBtnSave().setText(Messages.Backend_ButtonContinueText);
				break;
			}
			case 3: visual.getBtnSave().setText(Messages.Backend_ButtonCheckText);
		}
		
		visual.updateProgress();
		updateModus();
	}

	private void setColor(Color farbe) {
		if (farbe != STANDARD) {
			for (int i = 0; i < order.length; i++) {
				if (order[i] != 0) {
					if (farbe == GELB) {
						if(visual.getCntrBtn()[order[i] - 1].getImage() != null) visual.getCntrBtn()[order[i] - 1].getImage().dispose(); //dispose old image
						Image img = AndroidUnlockPatternPlugin
								.getImageDescriptor("icons/yellow.png").createImage(visual.getCntrBtn()[i].getDisplay()); //$NON-NLS-1$
						visual.getCntrBtn()[order[i] - 1].setImage(img);
						visual.getCntrBtn()[order[i] - 1].setData(
								"icon", "icons/yellow.png"); //$NON-NLS-1$ //$NON-NLS-2$
					} else if (farbe == ROT) {
						if(visual.getCntrBtn()[order[i] - 1].getImage() != null) visual.getCntrBtn()[order[i] - 1].getImage().dispose(); //dispose old image
						Image img = AndroidUnlockPatternPlugin
								.getImageDescriptor("icons/red.png").createImage(visual.getCntrBtn()[i].getDisplay()); //$NON-NLS-1$
						visual.getCntrBtn()[order[i] - 1].setImage(img);
						visual.getCntrBtn()[order[i] - 1].setData(
								"icon", "icons/red.png"); //$NON-NLS-1$ //$NON-NLS-2$

					} else if (farbe == GRUEN) {
						if(visual.getCntrBtn()[order[i] - 1].getImage() != null) visual.getCntrBtn()[order[i] - 1].getImage().dispose(); //dispose old image
						Image img = AndroidUnlockPatternPlugin
								.getImageDescriptor("icons/green.png").createImage(visual.getCntrBtn()[i].getDisplay()); //$NON-NLS-1$
						visual.getCntrBtn()[order[i] - 1].setImage(img);
						visual.getCntrBtn()[order[i] - 1].setData(
								"icon", "icons/green.png"); //$NON-NLS-1$ //$NON-NLS-2$

					}
					// visual.getCntrBtn()[order[i] - 1].setBackground(farbe);
					lineColor = farbe;
					visual.getCenterbox().redraw();
				}
			}
		} else {
			for (Label btn : visual.getCntrBtn()) {
				if(btn.getImage() != null)
					btn.getImage().dispose();
				Image img = AndroidUnlockPatternPlugin
						.getImageDescriptor("icons/black.png").createImage(btn.getDisplay()); //$NON-NLS-1$
				btn.setImage(img);
				btn.setData("icon", "icons/black.png"); //$NON-NLS-1$ //$NON-NLS-2$

			}
			// lineColor=farbe;
		}
		visual.getCenterbox().redraw();
	}

	private void resetBtn() {
		for (Label btn : visual.getCntrBtn()) {
//			btn.setEnabled(true);
			btn.setData("arc", null);
		}
//		for (int[] line : lines) {
//			line[0] = 0;
//			line[1] = 0;
//		}
//		recalculateLines();
		setColor(STANDARD);

	}

	private void resetOrder() {
		order = new int[10];
		for (int i = 0; i < order.length; i++) {
			order[i] = 0;
		}
		length = 0;

	}

	/**
	 * checks which Modus is active and set it in the UI also disables
	 * non-available modes
	 * 
	 */
	public void updateModus() {
//		visual.getSetPattern().setEnabled(true);
//		visual.getChangePattern().setEnabled(true);
//		visual.getCheckPattern().setEnabled(true);
		if (getModus() == 1) { // Modus SET
			visual.getSetPattern().setSelection(true);
			visual.getChangePattern().setSelection(false);
			visual.getCheckPattern().setSelection(false);
			visual.getSetPattern().setEnabled(true);
			visual.getChangePattern().setEnabled(false);
			visual.getCheckPattern().setEnabled(false);
		} else if (getModus() == 2) { // Modus change
			visual.getSetPattern().setSelection(false);
			visual.getChangePattern().setSelection(true);
			visual.getCheckPattern().setSelection(false);
			visual.getSetPattern().setEnabled(false);
			visual.getChangePattern().setEnabled(true);
			visual.getCheckPattern().setEnabled(true);
		} else if (getModus() == 3) { // Modus check
			visual.getSetPattern().setSelection(false);
			visual.getChangePattern().setSelection(false);
			visual.getCheckPattern().setSelection(true);
			visual.getSetPattern().setEnabled(false);
			visual.getChangePattern().setEnabled(true);
			visual.getCheckPattern().setEnabled(true);
		} else { // Fehlerfall
			LogUtil.logError("schwerer Fehler in \"checkModus\" bitte den Entwickler kontaktieren - Please contact the developer"); //$NON-NLS-1$
		}
	}

	/**
	 * saves order[] in file "./Android" separated by semicolon
	 * 
	 * @return false on error else true
	 */
	public boolean save() {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
					"./Android"))); //$NON-NLS-1$
			for (int i = 0; i < order.length; i++) {
				writer.write(order[i] + ";"); //$NON-NLS-1$
			}
			writer.close();
		} catch (IOException e) {
			LogUtil.logError("Error when saving file\n" + e.getMessage()); //$NON-NLS-1$
			return false;
		}
		return true;
	}

	/**
	 * read from file "./Android" and save in order[]
	 */
	public void restore() {
		BufferedReader reader;
		ordersaved = new int[10];
		try {
			reader = new BufferedReader(new FileReader(new File("./Android"))); //$NON-NLS-1$
			String zeile = reader.readLine();
			while (zeile != null) {
				String[] values = zeile.split(";"); //$NON-NLS-1$
				for (int i = 0; i < values.length && i < 10; i++) {
					ordersaved[i] = Integer.parseInt(values[i]);
				}
				zeile = reader.readLine();
			}
		} catch (IOException e) {

			// e.printStackTrace();
			LogUtil.logWarning("SaveFile not found. Pattern must be saved at first."); //$NON-NLS-1$
		}
	}

	/**
	 * @return modus
	 */
	public int getModus() {
		return modus;
	}

	/**
	 * @param modus
	 *            the modus to set
	 */
	public void setModus(int modus) {
		this.modus = modus;
		modusChanged();
	}

	public void recalculateLines() {
		for (int i = 0; i < points.length; i++) {
			for (int j = 0; j < points[i].length; j++) {
				points[i][j] = 0;
			}
		}
//		for (int i = 0; i < lines.length; i++) {
//			if (lines[i][0] != 0 && lines[i][1] != 0) {
//				points[i][0] = visual.getCntrBtn()[lines[i][0]-1].getLocation().x + visual.getCntrBtn()[lines[i][0]-1].getSize().x / 2;
//				points[i][1] = visual.getCntrBtn()[lines[i][0]-1].getLocation().y + visual.getCntrBtn()[lines[i][0]-1].getSize().y / 2;
//				points[i][2] = visual.getCntrBtn()[lines[i][1]-1].getLocation().x + visual.getCntrBtn()[lines[i][1]-1].getSize().x / 2;
//				points[i][3] = visual.getCntrBtn()[lines[i][1]-1].getLocation().y + visual.getCntrBtn()[lines[i][1]-1].getSize().y / 2;
//			}
//		}
		for (int i = 0; i < length - 1; i++) {
			points[i][0] = visual.getCntrBtn()[order[i]-1].getLocation().x + visual.getCntrBtn()[order[i]-1].getSize().x / 2;
			points[i][1] = visual.getCntrBtn()[order[i]-1].getLocation().y + visual.getCntrBtn()[order[i]-1].getSize().y / 2;
			points[i][2] = visual.getCntrBtn()[order[i+1]-1].getLocation().x + visual.getCntrBtn()[order[i+1]-1].getSize().x / 2;
			points[i][3] = visual.getCntrBtn()[order[i+1]-1].getLocation().y + visual.getCntrBtn()[order[i+1]-1].getSize().y / 2;
		}
	}

	public Color getLineColor() {
		return lineColor;
	}
	
	protected boolean isChangeable() {
		return isChangeable;
	}
	
	protected boolean isFirst() {
		return first;
	}
}
