/*
 * Rapid Beans Framework: EditorBeanSwingTest.java
 *
 * Copyright Martin Bluemel, 2006
 *
 * 18.08.2006
 */

package org.rapidbeans.presentation.swing;

import java.util.HashMap;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.tree.TreePath;

import junit.framework.TestCase;

import org.rapidbeans.core.basic.RapidBean;
import org.rapidbeans.core.type.TestHelperTypeLoader;
import org.rapidbeans.presentation.ApplicationManager;
import org.rapidbeans.presentation.DocumentView;
import org.rapidbeans.presentation.EditorProperty;

/**
 * Tests for the bean editor.
 *
 * @author Martin Bluemel
 */
public class EditorBeanSwingTest extends TestCase {

    /**
     * verify that the button text changes if you type something
     * invalid.
     *
     * @throws InterruptedException test
     */
    public void testEditButtonTexts() throws InterruptedException {
        DocumentTreeViewSwing docTreeView = PresentationSwingTestHelper.createTestTreeView();
        JTree tree = docTreeView.getTree();
        try {
            // expand "trainers" branch in the tree
            tree.expandPath(tree.getPathForRow(1));

            // edit a trainer
            TreePath path = tree.getPathForRow(2);
            Object[] keys = {path};
            RapidBean bean = (RapidBean) path.getLastPathComponent();
            Object[] selObjs = {bean};
            assertEquals("trainers", PresentationSwingTestHelper.getColPropName(tree, 1));
            assertEquals("Blümel", bean.getProperty("lastname").getValue());
            docTreeView.editBeans(keys, selObjs);
            DocumentView docView = PresentationSwingTestHelper.getTestDocumentView();
            EditorBeanSwing editor = (EditorBeanSwing) docView.getEditor(bean, false);
            assertSame(bean, editor.getBean());
            List<EditorProperty> propEditors = editor.getPropEditors();
            assertEquals(5, propEditors.size());

            // assert nothing has changed
            assertFalse(editor.isAnyInputFieldChanged());
            HashMap<String, Object> buttons = editor.getButtonWidgets();
            assertEquals("OK", ((JButton) buttons.get("ok")).getText());
            assertEquals(false, ((JButton) buttons.get("ok")).isEnabled());
            assertEquals("Apply", ((JButton) buttons.get("apply")).getText());
            assertEquals(false, ((JButton) buttons.get("apply")).isEnabled());
            assertEquals("Close", ((JButton) buttons.get("close")).getText());
            assertEquals(true, ((JButton) buttons.get("close")).isEnabled());

            // type an invalid email
            EditorPropertyTextSwing propEditor =
                (EditorPropertyTextSwing) propEditors.get(3);
            assertEquals("email", propEditor.getProperty().getType().getPropName());
            ((JTextField) propEditor.getWidget()).setText("X");
            propEditor.fireInputFieldChanged();

            // assert that something has changed and that the buttons
            // a correcly enabled / disabled and have correct texts
            assertTrue(editor.isAnyInputFieldChanged());
            assertEquals("OK", ((JButton) buttons.get("ok")).getText());
            assertEquals(false, ((JButton) buttons.get("ok")).isEnabled());
            assertEquals("Check", ((JButton) buttons.get("apply")).getText());
            assertEquals(true, ((JButton) buttons.get("apply")).isEnabled());
            assertEquals("Cancel", ((JButton) buttons.get("close")).getText());
            assertEquals(true, ((JButton) buttons.get("close")).isEnabled());
        } finally {
            PresentationSwingTestHelper.deleteTestTreeView();
        }
    }

//    /**
//     * verify that the button text changes if you type something
//     * invalid.
//     *
//     * @throws InterruptedException test
//     */
//    public void testCreateButtonTexts() throws InterruptedException {
//        DocumentTreeViewSwing docTreeView = PresentationSwingTestHelper.createTestTreeView();
//        JTree tree = docTreeView.getTree();
//        try {
//            // select "trainers" in the tree
//            tree.setSelectionPath(tree.getPathForRow(1));
//
//            // open the dialog for creating a new trainer
//            EditorBeanSwing editor = (EditorBeanSwing) docTreeView.createBean();
//            HashMap<String, Object> buttons = editor.getButtonWidgets();
//            assertFalse(editor.isAnyInputFieldChanged());
//            assertEquals(null, editor.getBean().getProperty("lastname").getValue());
//            assertEquals("", ((JTextField) editor.getPropEditors().get(0).getWidget()).getText());
//            assertEquals(null, editor.getBean().getProperty("firstname").getValue());
//            assertEquals("", ((JTextField) editor.getPropEditors().get(1).getWidget()).getText());
//            assertEquals("OK", ((JButton) buttons.get("ok")).getText());
//            assertEquals(false, ((JButton) buttons.get("ok")).isEnabled());
//            assertEquals("Apply", ((JButton) buttons.get("apply")).getText());
//            assertEquals(false, ((JButton) buttons.get("apply")).isEnabled());
//            assertEquals("Close", ((JButton) buttons.get("close")).getText());
//            assertEquals(true, ((JButton) buttons.get("close")).isEnabled());
//
//            // set the key fiels to values of an already existing bean
//            ((JTextField) editor.getPropEditors().get(0).getWidget()).setText("Blümel");
//            assertEquals("Blümel", ((JTextField) editor.getPropEditors().get(0).getWidget()).getText());
//            editor.getPropEditors().get(0).fireInputFieldChanged();
//            assertEquals("Blümel", editor.getBean().getProperty("lastname").getValue());
//            assertEquals(null, editor.getBean().getProperty("firstname").getValue());
//            assertEquals("", ((JTextField) editor.getPropEditors().get(1).getWidget()).getText());
//            assertTrue(editor.isAnyInputFieldChanged());
//            assertEquals("OK", ((JButton) buttons.get("ok")).getText());
//            assertEquals(false, ((JButton) buttons.get("ok")).isEnabled());
//            assertEquals("Check", ((JButton) buttons.get("apply")).getText());
//            assertEquals(true, ((JButton) buttons.get("apply")).isEnabled());
//            assertEquals("Cancel", ((JButton) buttons.get("close")).getText());
//            assertEquals(true, ((JButton) buttons.get("close")).isEnabled());
//
//            ((JTextField) editor.getPropEditors().get(1).getWidget()).setText("Martin");
//            editor.getPropEditors().get(1).fireInputFieldChanged();
//            // assert that something has changed and that the buttons
//            // a correcly enabled / disabled and have correct texts
//            // Since "Martin Blümel" already exists the apply button
//            // should have the "Check" text.
//            assertTrue(editor.isAnyInputFieldChanged());
//            assertEquals("OK", ((JButton) buttons.get("ok")).getText());
//            assertEquals(false, ((JButton) buttons.get("ok")).isEnabled());
//            assertEquals("Check", ((JButton) buttons.get("apply")).getText());
//            assertEquals(true, ((JButton) buttons.get("apply")).isEnabled());
//            assertEquals("Cancel", ((JButton) buttons.get("close")).getText());
//            assertEquals(true, ((JButton) buttons.get("close")).isEnabled());
//        } finally {
//            PresentationSwingTestHelper.deleteTestTreeView();
//        }
//    }
//
//    /**
//     * verify that an input field is recognized as changed,
//     * when a editor has been opened for creating a bean
//     * and something has been changed.
//     *
//     * - select the trainers property in the most upper
//     *   level of properties.
//     * - open the editor for creating
//     * - verify that no input field has changed
//     * - verify that an input field can be recognized as changed
//     *   after a change of the check boxes
//     *
//     * @throws InterruptedException test
//     */
//    public void testCreateIsAnyInputFieldChangedChangeCheckBoxProperty() throws InterruptedException {
//        DocumentTreeViewSwing docTreeView =
//            PresentationSwingTestHelper.createTestTreeView();
//        JTree tree = docTreeView.getTree();
//        try {
//            assertEquals("trainers", PresentationSwingTestHelper.getColPropName(tree, 1));
//            TreePath path = tree.getPathForRow(1);
//            Object[] keys = {path};
//            PropertyCollection prop = ((DocumentTreeNodePropColComp) path.getLastPathComponent()).getColProp();
//            assertEquals("trainers", prop.getType().getPropName());
//            Object key = keys[0];
//            docTreeView.createbeany, prop);
//            DocumentView docView = PresentationSwingTestHelper.getTestDocumentView();
//            RapidBean newBean = RapidBeanImplStrict.createInstance(((TypePropertyCollection) prop.getType()).getTargetType().getName());
//            EditorBeanSwing editor = (EditorBeanSwing) docView.getEditor(newBean, true);
//            assertNotSame(newBean, editor.getBean());
//            assertEquals(newBean, editor.getBean());
//            List<EditorProperty> propEditors = editor.getPropEditors();
//            assertEquals(5, propEditors.size());
//            assertFalse(editor.isAnyInputFieldChanged());
//            HashMap<String, Object> buttons = editor.getButtonWidgets();
//            assertEquals("OK", ((JButton) buttons.get("ok")).getText());
//            assertEquals(false, ((JButton) buttons.get("ok")).isEnabled());
//            assertEquals("Apply", ((JButton) buttons.get("apply")).getText());
//            assertEquals(false, ((JButton) buttons.get("apply")).isEnabled());
//            assertEquals("Close", ((JButton) buttons.get("close")).getText());
//            assertEquals(true, ((JButton) buttons.get("close")).isEnabled());
//
//            EditorPropertyTextSwing te =
//                (EditorPropertyTextSwing) propEditors.get(0);
//            ((JTextField) te.getWidget()).setText("Habicht");
//            te =
//                (EditorPropertyTextSwing) propEditors.get(1);
//            ((JTextField) te.getWidget()).setText("Hugo");
//
//            EditorPropertyCheckboxSwing leaderEditor =
//                (EditorPropertyCheckboxSwing) propEditors.get(2);
//            PropertyBoolean leaderProp = (PropertyBoolean) leaderEditor.getProperty();
//            assertEquals("leader", leaderProp.getType().getPropName());
//            assertEquals(false, leaderProp.getValueBoolean());
//            JCheckBox leaderCheckBox = (JCheckBox) leaderEditor.getWidget();
//            leaderCheckBox.setSelected(true);
//            assertTrue(editor.isAnyInputFieldChanged());
//
//            assertEquals("OK", ((JButton) buttons.get("ok")).getText());
//            assertEquals(true, ((JButton) buttons.get("ok")).isEnabled());
//            assertEquals("Apply", ((JButton) buttons.get("apply")).getText());
//            assertEquals(true, ((JButton) buttons.get("apply")).isEnabled());
//            assertEquals("Cancel", ((JButton) buttons.get("close")).getText());
//            assertEquals(true, ((JButton) buttons.get("close")).isEnabled());
//        } finally {
//            PresentationSwingTestHelper.deleteTestTreeView();
//        }
//    }
//
//    /**
//     * Edit a bean and press the apply button.
//     *
//     * - the changes are transferred to the bean during
//     *   typing valid values.
//     * - apply does not add to much to it but adapts the
//     *   bean backup so that the editor does not present the
//     *   bean as changed anymore
//     *
//     * @throws InterruptedException test
//     */
//    public void testEditApply() throws InterruptedException {
//        DocumentTreeViewSwing docTreeView =
//            PresentationSwingTestHelper.createTestTreeView();
//        JTree tree = docTreeView.getTree();
//        try {
//            assertEquals("trainers", PresentationSwingTestHelper.getColPropName(tree, 1));
//            tree.expandPath(tree.getPathForRow(1));
//            tree.setSelectionPath(tree.getPathForRow(2));
//            docTreeView.editbean;
//            DocumentView docView = PresentationSwingTestHelper.getTestDocumentView();
//            EditorBeanSwing editor = (EditorBeanSwing) docView.getEditor(
//                    (RapidBean) tree.getPathForRow(2).getLastPathComponent(), false);
//            List<EditorProperty> propEditors = editor.getPropEditors();
//
//            // assert that for the editor
//            // - no input field is changed
//            // - the OK button is insensitive
//            // - the Apply button is insensitive
//            // - the Close button shows the text "Close"
//            assertFalse(editor.isAnyInputFieldChanged());
//            HashMap<String, Object> buttons = editor.getButtonWidgets();
//            assertEquals("OK", ((JButton) buttons.get("ok")).getText());
//            assertEquals(false, ((JButton) buttons.get("ok")).isEnabled());
//            assertEquals("Apply", ((JButton) buttons.get("apply")).getText());
//            assertEquals(false, ((JButton) buttons.get("apply")).isEnabled());
//            assertEquals("Close", ((JButton) buttons.get("close")).getText());
//            assertEquals(true, ((JButton) buttons.get("close")).isEnabled());
//
//            EditorPropertyCheckboxSwing leaderEditor =
//                (EditorPropertyCheckboxSwing) propEditors.get(2);
//            JCheckBox leaderCheckBox = (JCheckBox) leaderEditor.getWidget();
//            assertTrue(leaderCheckBox.isSelected());
//            leaderCheckBox.setSelected(false);
//
//            assertTrue(editor.isAnyInputFieldChanged());
//
//            // assert that for the editor
//            // - an input field is changed
//            // - the OK button is sensitive
//            // - the Apply button is sensitive
//            // - the Close button shows the text "Cancel"
//            assertEquals("OK", ((JButton) buttons.get("ok")).getText());
//            assertEquals(true, ((JButton) buttons.get("ok")).isEnabled());
//            assertEquals("Apply", ((JButton) buttons.get("apply")).getText());
//            assertEquals(true, ((JButton) buttons.get("apply")).isEnabled());
//            assertEquals("Cancel", ((JButton) buttons.get("close")).getText());
//            assertEquals(true, ((JButton) buttons.get("close")).isEnabled());
//
//            editor.handleActionApply();
//
//            // assert that for the editor
//            // - no input field is changed
//            // - the OK button is insensitive
//            // - the Apply button is insensitive
//            // - the Close button shows the text "Close"
//            assertFalse(editor.isAnyInputFieldChanged());
//            assertEquals("OK", ((JButton) buttons.get("ok")).getText());
//            assertEquals(false, ((JButton) buttons.get("ok")).isEnabled());
//            assertEquals("Apply", ((JButton) buttons.get("apply")).getText());
//            assertEquals(false, ((JButton) buttons.get("apply")).isEnabled());
//            assertEquals("Close", ((JButton) buttons.get("close")).getText());
//            assertEquals(true, ((JButton) buttons.get("close")).isEnabled());
//        } finally {
//            PresentationSwingTestHelper.deleteTestTreeView();
//        }
//    }
//
//    /**
//     * verify the typing behavour and the colors during creating a new bean.
//     *
//     * - select the trainers property in the most upper
//     *   level of properties.
//     * - open the editor for creating
//     * - verify the initial colors
//     *
//     * @throws InterruptedException test
//     */
//    public void testCreateTypingAndColorsInit() throws InterruptedException {
//        DocumentTreeViewSwing docTreeView =
//            PresentationSwingTestHelper.createTestTreeView();
//        JTree tree = docTreeView.getTree();
//        try {
//            assertEquals("trainers", PresentationSwingTestHelper.getColPropName(tree, 1));
//            TreePath path = tree.getPathForRow(1);
//            Object[] keys = {path};
//            PropertyCollection prop = ((DocumentTreeNodePropColComp)
//                    path.getLastPathComponent()).getColProp();
//            Object key = keys[0];
//            docTreeView.createbeany, prop);
//            DocumentView docView = PresentationSwingTestHelper.getTestDocumentView();
//            RapidBean newBean = RapidBeanImplStrict.createInstance(((TypePropertyCollection) prop.getType()).getTargetType().getName());
//            EditorBeanSwing editor = (EditorBeanSwing) docView.getEditor(newBean, true);
//            assertNotSame(newBean, editor.getBean());
//            assertEquals(newBean, editor.getBean());
//            for (EditorProperty propEditor : editor.getPropEditors()) {
//                if (propEditor.getProperty().getType().getPropName().
//                        equals("lastname")) {
//                    assertNull(propEditor.getInputFieldValue());
//                    JTextField tf = (JTextField) propEditor.getWidget();
//                    assertEquals(EditorPropertySwing.COLOR_KEY, tf.getBackground());
//                }
//                if (propEditor.getProperty().getType().getPropName().
//                        equals("firstname")) {
//                    assertNull(propEditor.getInputFieldValue());
//                    JTextField tf = (JTextField) propEditor.getWidget();
//                    assertEquals(EditorPropertySwing.COLOR_KEY, tf.getBackground());
//                }
//                if (propEditor.getProperty().getType().getPropName().
//                        equals("leader")) {
//                    assertEquals(new Boolean(false), propEditor.getInputFieldValue());
//                    JCheckBox cb = (JCheckBox) propEditor.getWidget();
//                    assertEquals(EditorPropertySwing.COLOR_MANDATORY, cb.getBackground());
//                }
//                if (propEditor.getProperty().getType().getPropName().
//                        equals("certificates")) {
//                    assertEquals(new ArrayList<RapidBean>(), propEditor.getInputFieldValue());
//                    JPanel cb = (JPanel) propEditor.getWidget();
//                    assertEquals(EditorPropertySwing.COLOR_MANDATORY, cb.getBackground());
//                }
//            }
//        } finally {
//            PresentationSwingTestHelper.deleteTestTreeView();
//        }
//    }
//
//    /**
//     * verify the typing behavour and the colors during creating a new bean.
//     *
//     * - select the trainers property in the most upper
//     *   level of properties.
//     * - open the editor for creating
//     * - verify the initial colors
//     *
//     * @throws InterruptedException test
//     */
//    public void testCreateTypingAndColorsType() throws InterruptedException {
//        DocumentTreeViewSwing docTreeView =
//            PresentationSwingTestHelper.createTestTreeView();
//        JTree tree = docTreeView.getTree();
//        try {
//            assertEquals("trainers", PresentationSwingTestHelper.getColPropName(tree, 1));
//            TreePath path = tree.getPathForRow(1);
//            Object[] keys = {path};
//            PropertyCollection prop = ((DocumentTreeNodePropColComp) path.getLastPathComponent()).getColProp();
//            Object key = keys[0];
//            docTreeView.createbeany, prop);
//            DocumentView docView = PresentationSwingTestHelper.getTestDocumentView();
//            RapidBean newBean = RapidBeanImplStrict.createInstance(((TypePropertyCollection) prop.getType()).getTargetType().getName());
//            EditorBeanSwing editor = (EditorBeanSwing) docView.getEditor(newBean, true);
//            assertNotSame(newBean, editor.getBean());
//            assertEquals(newBean, editor.getBean());
//            for (EditorProperty propEditor : editor.getPropEditors()) {
//                if (propEditor.getProperty().getType().getPropName().
//                        equals("lastname")) {
//                    assertNull(propEditor.getInputFieldValue());
//                    JTextField tf = (JTextField) propEditor.getWidget();
//                    assertEquals(EditorPropertySwing.COLOR_KEY, tf.getBackground());
//                }
//                if (propEditor.getProperty().getType().getPropName().
//                        equals("firstname")) {
//                    assertNull(propEditor.getInputFieldValue());
//                    JTextField tf = (JTextField) propEditor.getWidget();
//                    assertEquals(EditorPropertySwing.COLOR_KEY, tf.getBackground());
//                }
//                if (propEditor.getProperty().getType().getPropName().
//                        equals("leader")) {
//                    assertEquals(new Boolean(false), propEditor.getInputFieldValue());
//                    JCheckBox cb = (JCheckBox) propEditor.getWidget();
//                    assertEquals(EditorPropertySwing.COLOR_MANDATORY, cb.getBackground());
//                }
//                if (propEditor.getProperty().getType().getPropName().
//                        equals("certificates")) {
//                    assertEquals(new ArrayList<RapidBean>(), propEditor.getInputFieldValue());
//                    JPanel cb = (JPanel) propEditor.getWidget();
//                    assertEquals(EditorPropertySwing.COLOR_MANDATORY, cb.getBackground());
//                }
//            }
//        } finally {
//            PresentationSwingTestHelper.deleteTestTreeView();
//        }
//    }
//
//    /**
//     * verify that no input field has changed, when a editor
//     * has been opened for editing.
//     *
//     * - select a trainer in the, tree view
//     * - open the editor for editing
//     * - verify that no input field has changed
//     *
//     * @throws InterruptedException test
//     */
//    public void testEditIsAnyInputFieldChangedChangeCheckBoxProperty() throws InterruptedException {
//        DocumentTreeViewSwing docTreeView = PresentationSwingTestHelper.createTestTreeView();
//        JTree tree = docTreeView.getTree();
//        try {
//            // expand "treiners" branch in the tree
//            tree.expandPath(tree.getPathForRow(1));
//            TreePath path = tree.getPathForRow(2);
//            Object[] keys = {path};
//            RapidBean bean = (RapidBean) path.getLastPathComponent();
//            Object[] selObjs = {bean};
//            assertEquals("trainers", PresentationSwingTestHelper.getColPropName(tree, 1));
//            assertEquals("Blümel", bean.getProperty("lastname").getValue());
//            docTreeView.editbeaneys, selObjs);
//            DocumentView docView = PresentationSwingTestHelper.getTestDocumentView();
//            EditorBeanSwing editor = (EditorBeanSwing) docView.getEditor(bean, false);
//            assertSame(bean, editor.getBean());
//            List<EditorProperty> propEditors = editor.getPropEditors();
//
//            assertFalse(editor.isAnyInputFieldChanged());
//            HashMap<String, Object> buttons = editor.getButtonWidgets();
//            assertEquals("OK", ((JButton) buttons.get("ok")).getText());
//            assertEquals(false, ((JButton) buttons.get("ok")).isEnabled());
//            assertEquals("Apply", ((JButton) buttons.get("apply")).getText());
//            assertEquals(false, ((JButton) buttons.get("apply")).isEnabled());
//            assertEquals("Close", ((JButton) buttons.get("close")).getText());
//            assertEquals(true, ((JButton) buttons.get("close")).isEnabled());
//
//            EditorPropertyCheckboxSwing leaderEditor =
//                (EditorPropertyCheckboxSwing) propEditors.get(2);
//            PropertyBoolean leaderProp = (PropertyBoolean) leaderEditor.getProperty();
//            assertEquals("leader", leaderProp.getType().getPropName());
//            assertEquals(true, leaderProp.getValueBoolean());
//            JCheckBox leaderCheckBox = (JCheckBox) leaderEditor.getWidget();
//            leaderCheckBox.setSelected(false);
//            assertTrue(editor.isAnyInputFieldChanged());
//            buttons = editor.getButtonWidgets();
//            assertEquals("OK", ((JButton) buttons.get("ok")).getText());
//            assertEquals(true, ((JButton) buttons.get("ok")).isEnabled());
//            assertEquals("Apply", ((JButton) buttons.get("apply")).getText());
//            assertEquals(true, ((JButton) buttons.get("apply")).isEnabled());
//            assertEquals("Cancel", ((JButton) buttons.get("close")).getText());
//            assertEquals(true, ((JButton) buttons.get("close")).isEnabled());
//        } finally {
//            PresentationSwingTestHelper.deleteTestTreeView();
//        }
//    }

    public void setUp() {
        PresentationSwingTestHelper.createCertificate("XXX");
        PresentationSwingTestHelper.createTrainer("Uga", "Aga", true, true);
        PresentationSwingTestHelper.createTrainingDate("xxx", "monday", "12:30", "13:30", null);
    }

    /**
     * unregister type "Trainer".
     */
    public void tearDown() {
        TestHelperTypeLoader.clearBeanTypesGeneric();
        if (ApplicationManager.getApplication() != null) {
            ApplicationManager.resetApplication();
        }        
    }
}
