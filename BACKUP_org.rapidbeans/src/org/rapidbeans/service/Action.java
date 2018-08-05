/*
 * Partially generated code file: Action.java
 * !!!Do only edit manually in marked sections!!!
 *
 * Rapid Beans 
				bean
			 generator, Copyright Martin Bluemel, 2008
 *
 * generated Java implementation of Rapid Beans 
				bean
			 type
 * org.rapidbeans.service.Action
 * 
 * model:    model/org/rapidbeans/service/Action.xml
 * template: codegentemplates/genBean.xsl
 */
package org.rapidbeans.service;



// BEGIN manual code section
// Action.import
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.MissingResourceException;

import org.rapidbeans.core.basic.Link;
import org.rapidbeans.core.basic.LinkFrozen;
import org.rapidbeans.core.basic.RapidBeanImplStrict;
import org.rapidbeans.core.exception.RapidBeansRuntimeException;
import org.rapidbeans.core.exception.UnresolvedLinkException;
import org.rapidbeans.core.type.TypeRapidBean;
import org.rapidbeans.presentation.Application;
import org.rapidbeans.presentation.ApplicationManager;

// END manual code section

/**
 * Rapid Bean class: Action.
 * Partially generated Java class
 * !!!Do only edit manually in marked sections!!!
 **/
public class Action extends RapidBeanImplStrict {
	// BEGIN manual code section
	// Action.classBody
	/**
	 * as long as we do not have a map.
	 * 
	 * @param name
	 *            the argument name
	 * 
	 * @return the value of the action argument with the given name or null if
	 *         not found
	 */
	public String getArgumentValue(final String name) {
		final Collection<ActionArgument> aarguments = this.getArguments();
		if (aarguments != null) {
			for (ActionArgument aa : aarguments) {
				if (aa.getName() != null && aa.getName().equals(name)) {
					return aa.getValue();
				}
			}
		}
		return null;
	}

	/**
	 * argument getter with default value.
	 * 
	 * @param name
	 *            the argument name
	 * @param defaultval
	 *            the default value if the argument is not defined
	 * 
	 * @return the value of the action argument with the given name or the given
	 *         default if not found
	 */
	public String getArgumentValue(final String name, final String defaultval) {
		final String argval = this.getArgumentValue(name);
		if (argval != null) {
			return argval;
		} else {
			return defaultval;
		}
	}

	/**
	 * the execute method of every action.
	 */
	public void execute() {
		final Application app = ApplicationManager.getApplication();
		app.messageInfo(app.getCurrentLocale().getStringMessage("action.notyetimpl", this.getClassname()));
	}

	/**
	 * default constructor parameter types.
	 */
	private static final Class<?>[] CONSTRUCTOR_PARAM_TYPES = new Class[0];

	/**
	 * default constructor parameter types.
	 */
	private static final Object[] CONSTRUCTOR_PARAMS = new Object[0];

	/**
	 * creates an instance out of an action configuration.
	 * 
	 * @return the new ActionInstance
	 */
	public final Action clone() {
		Action action = null;
		// construct action via reflection
		try {
			final Class<?> actionClass = Class.forName(this.getClassname());
			Constructor<?> constructor = actionClass.getConstructor(CONSTRUCTOR_PARAM_TYPES);
			action = (Action) constructor.newInstance(CONSTRUCTOR_PARAMS);
			if (this.getArguments() != null) {
				for (final ActionArgument aa : this.getArguments()) {
					final ActionArgument arg = new ActionArgument();
					arg.setName(aa.getName());
					arg.setValue(aa.getValue());
					action.addArgument(arg);
				}
			}
			action.setRolesrequired(this.getRolesrequired());
		} catch (ClassNotFoundException e) {
			throw new RapidBeansRuntimeException("Action class not found: " + this.getClassname(), e);
		} catch (NoSuchMethodException e) {
			throw new RapidBeansRuntimeException(e.getClass().getName() + ": " + e.getMessage());
		} catch (IllegalAccessException e) {
			throw new RapidBeansRuntimeException(e.getClass().getName() + ": " + e.getMessage());
		} catch (InstantiationException e) {
			throw new RapidBeansRuntimeException(e.getClass().getName() + ": " + e.getMessage());
		} catch (InvocationTargetException e) {
			throw new RapidBeansRuntimeException(e.getClass().getName() + ": " + e.getMessage());
		}
		action.setClassname(this.getClassname());
		action.setWaitcursor(this.getWaitcursor());
		action.setBackground(this.getBackground());
		action.setProgressbar(this.getProgressbar());
		return action;
	}

	public String getMessage() {
		final Application app = ApplicationManager.getApplication();
		String message = null;
		try {
			message = app.getCurrentLocale().getStringGui("action." + getClassname() + ".message." + getState().name());
		} catch (MissingResourceException me) {
			message = null;
		}
		if (message == null) {
			message = app.getCurrentLocale().getStringMessage("action.execution." + getState().name(),
					toStringGuiId(app.getCurrentLocale()));
		}
		return message;
	}

	// END manual code section

	/**
	 * property "classname".
	 */
	private org.rapidbeans.core.basic.PropertyString classname;

	/**
	 * property "arguments".
	 */
	private org.rapidbeans.core.basic.PropertyAssociationend arguments;

	/**
	 * property "state".
	 */
	private org.rapidbeans.core.basic.PropertyChoice state;

	/**
	 * property "waitcursor".
	 */
	private org.rapidbeans.core.basic.PropertyBoolean waitcursor;

	/**
	 * property "background".
	 */
	private org.rapidbeans.core.basic.PropertyBoolean background;

	/**
	 * property "progressbar".
	 */
	private org.rapidbeans.core.basic.PropertyBoolean progressbar;

	/**
	 * property "rolesrequired".
	 */
	private org.rapidbeans.core.basic.PropertyChoice rolesrequired;

	/**
	 * property "enabler".
	 */
	private org.rapidbeans.core.basic.PropertyAssociationend enabler;

	/**
	 * property references initialization.
	 */
	public void initProperties() {
		this.classname = (org.rapidbeans.core.basic.PropertyString)
			this.getProperty("classname");
		this.arguments = (org.rapidbeans.core.basic.PropertyAssociationend)
			this.getProperty("arguments");
		this.state = (org.rapidbeans.core.basic.PropertyChoice)
			this.getProperty("state");
		this.waitcursor = (org.rapidbeans.core.basic.PropertyBoolean)
			this.getProperty("waitcursor");
		this.background = (org.rapidbeans.core.basic.PropertyBoolean)
			this.getProperty("background");
		this.progressbar = (org.rapidbeans.core.basic.PropertyBoolean)
			this.getProperty("progressbar");
		this.rolesrequired = (org.rapidbeans.core.basic.PropertyChoice)
			this.getProperty("rolesrequired");
		this.enabler = (org.rapidbeans.core.basic.PropertyAssociationend)
			this.getProperty("enabler");
	}

	/**
	 * default constructor.
	 */
	public Action() {
		super();
		// BEGIN manual code section
		// Action.Action()
		if (this.getClassname() == null) {
			this.setClassname(this.getClass().getName());
		}
		// END manual code section
	}

	/**
	 * constructor out of a string.
	 * @param s
	 *            the string
	 */
	public Action(final String s) {
		super(s);
		// BEGIN manual code section
		// Action.Action(String)
		// END manual code section
	}

	/**
	 * constructor out of a string array.
	 * @param sa
	 *            the string array
	 */
	public Action(final String[] sa) {
		super(sa);
		// BEGIN manual code section
		// Action.Action(String[])
		// END manual code section
	}

	/**
	 * the bean's type (class variable).
	 */
	private static TypeRapidBean type = TypeRapidBean.createInstance(Action.class);

	/**
	 * @return the Biz Bean's type
	 */
	public TypeRapidBean getType() {
		return type;
	}

	/**
	 * @return value of Property 'classname'
	 */
	public String getClassname() {
		try {
			return (String) this.classname.getValue();
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException("classname");
		}
	}

	/**
	 * setter for Property 'classname'.
	 * @param argValue
	 *            value of Property 'classname' to set
	 */
	public void setClassname(final String argValue) {
		this.classname.setValue(argValue);
	}

	/**
	 * @return value of Property 'arguments'
	 */
	@SuppressWarnings("unchecked")
	public org.rapidbeans.core.common.ReadonlyListCollection<org.rapidbeans.service.ActionArgument> getArguments() {
		try {
			return (org.rapidbeans.core.common.ReadonlyListCollection<org.rapidbeans.service.ActionArgument>)
			this.arguments.getValue();
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException("arguments");
		}
	}

	/**
	 * setter for Property 'arguments'.
	 * @param argValue
	 *            value of Property 'arguments' to set
	 */
	public void setArguments(final java.util.Collection<org.rapidbeans.service.ActionArgument> argValue) {
		this.arguments.setValue(argValue);
	}
	/**
	 * add method for Property 'arguments'.
	 * @param bean
	 *            the bean to add
	 */
	public void addArgument(final org.rapidbeans.service.ActionArgument bean) {
		((org.rapidbeans.core.basic.PropertyCollection) this.arguments).addLink(bean);
	}
	/**
	 * remove method for Property 'arguments'.
	 * @param bean
	 *            the bean to remove
	 */
	public void removeArgument(final org.rapidbeans.service.ActionArgument bean) {
		((org.rapidbeans.core.basic.PropertyCollection) this.arguments).removeLink(bean);
	}

	/**
	 * @return value of Property 'state'
	 */
	public org.rapidbeans.service.ActionState getState() {
		try {
			java.util.List<?> enumList = (java.util.List<?>) this.state.getValue();
			if (enumList == null || enumList.size() == 0) {
				return null;
			} else {
				return (org.rapidbeans.service.ActionState) enumList.get(0);
			}
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException("state");
		}
	}

	/**
	 * setter for Property 'state'.
	 * @param argValue
	 *            value of Property 'state' to set
	 */
	public void setState(final org.rapidbeans.service.ActionState argValue) {
		java.util.List<org.rapidbeans.service.ActionState> list =
			new java.util.ArrayList<org.rapidbeans.service.ActionState>();
		list.add(argValue);
		this.state.setValue(list);
	}

	/**
	 * @return value of Property 'waitcursor'
	 */
	public boolean getWaitcursor() {
		try {
			return ((org.rapidbeans.core.basic.PropertyBoolean) this.waitcursor).getValueBoolean();
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException("waitcursor");
		}
	}

	/**
	 * setter for Property 'waitcursor'.
	 * @param argValue
	 *            value of Property 'waitcursor' to set
	 */
	public void setWaitcursor(final boolean argValue) {
		this.waitcursor.setValue(new Boolean(argValue));
	}

	/**
	 * @return value of Property 'background'
	 */
	public boolean getBackground() {
		try {
			return ((org.rapidbeans.core.basic.PropertyBoolean) this.background).getValueBoolean();
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException("background");
		}
	}

	/**
	 * setter for Property 'background'.
	 * @param argValue
	 *            value of Property 'background' to set
	 */
	public void setBackground(final boolean argValue) {
		this.background.setValue(new Boolean(argValue));
	}

	/**
	 * @return value of Property 'progressbar'
	 */
	public boolean getProgressbar() {
		try {
			return ((org.rapidbeans.core.basic.PropertyBoolean) this.progressbar).getValueBoolean();
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException("progressbar");
		}
	}

	/**
	 * setter for Property 'progressbar'.
	 * @param argValue
	 *            value of Property 'progressbar' to set
	 */
	public void setProgressbar(final boolean argValue) {
		this.progressbar.setValue(new Boolean(argValue));
	}

	/**
	 * @return value of Property 'rolesrequired'
	 */
	@SuppressWarnings("unchecked")
	public java.util.List<org.rapidbeans.core.basic.RapidEnum> getRolesrequired() {
		try {
			return (java.util.List<org.rapidbeans.core.basic.RapidEnum>) this.rolesrequired.getValue();
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException("rolesrequired");
		}
	}

	/**
	 * setter for Property 'rolesrequired'.
	 * @param argValue
	 *            value of Property 'rolesrequired' to set
	 */
	public void setRolesrequired(final java.util.List<org.rapidbeans.core.basic.RapidEnum> argValue) {
		this.rolesrequired.setValue(argValue);
	}

	/**
	 * @return value of Property 'enabler'
	 */
	@SuppressWarnings("unchecked")
	public org.rapidbeans.presentation.enabler.Enabler getEnabler() {
		try {
			org.rapidbeans.core.common.ReadonlyListCollection<org.rapidbeans.presentation.enabler.Enabler> col
				= (org.rapidbeans.core.common.ReadonlyListCollection<org.rapidbeans.presentation.enabler.Enabler>) this.enabler.getValue();
			if (col == null || col.size() == 0) {
				return null;
			} else {
				Link link = (Link) col.iterator().next();
				if (link instanceof LinkFrozen) {
					throw new UnresolvedLinkException("unresolved link to \""
							+ "org.rapidbeans.presentation.enabler.Enabler"
							+ "\" \"" + link.getIdString() + "\"");
				} else {
					return (org.rapidbeans.presentation.enabler.Enabler) col.iterator().next();
				}
			}
		} catch (NullPointerException e) {
			throw new org.rapidbeans.core.exception.PropNotInitializedException("enabler");
		}
	}

	/**
	 * setter for Property 'enabler'.
	 * @param argValue
	 *            value of Property 'enabler' to set
	 */
	public void setEnabler(final org.rapidbeans.presentation.enabler.Enabler argValue) {
		this.enabler.setValue(argValue);
	}
}
