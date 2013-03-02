/*
 * Rapid Beans Framework: PropertyAssociationend.java
 * 
 * Copyright (C) 2009 Martin Bluemel
 * 
 * Creation Date: 08/18/2009
 * 
 * This program is free software; you can redistribute it and/or modify it under the terms of the
 * GNU Lesser General Public License as published by the Free Software Foundation;
 * either version 3 of the License, or (at your option) any later version.
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU Lesser General Public License for more details.
 * You should have received a copies of the GNU Lesser General Public License and the
 * GNU General Public License along with this program; if not, see <http://www.gnu.org/licenses/>.
 */

package org.rapidbeans.core.basic;

import org.rapidbeans.core.type.TypeProperty;

/**
 * An <b>Associationend</b> bean property encapsulates a set of bean links or
 * references. By means of this property you can easily define association
 * between beans.<br/>
 * <br/>
 * <b>Associations</b> are much more than simply object references. They exactly
 * define the way how to establish a connection between instances. Besides
 * classes (beans) and attributes (properties) they are another core mean to
 * describe a data model in an object oriented manner.<br/>
 * <br>
 * In an <a href="http://www.devx.com/enterprise/Article/28528/1763">article</a>
 * Anneke Klepper and Jos Warner compare an association with a marriage which
 * has characteristics that could be described through the acronym
 * <b>ABACUS</b>: <it> <li><b>A</b>wareness: Both objects are aware of the fact that the relationship exists even if the is only one way navigability. That means an association between instance A and B will be automatically deleted if one of the instances is deleted.</li> <li><b>B</b>oolean existence: If the partners agree to a divorce, the relationship (in UML called the link) is dissolved completely.</li> <li><b>A</b>greement: Both objects have to agree with the relationship; they need to say
 * "I do."</li> <li><b>C</b>ascaded deletion: If one of the partners dies, the relationship is dissolved as well. That means the two links between instance A and B will be automatically deleted if one of the instances is deleted.</li> <li><b>US</b>e of <b>role names</b>: An object may refer to its partner using the role name provided with the association: "my husband" or "my wife." Therefore every association end defines with its property name a so called role name.</li> </it><br/>
 * <br/>
 * Associations are defined together with classes. Concrete connections between
 * instances we call links. Currently RapidBeans supports only binary
 * associations. That means an association instance (link) can only associate
 * two instances with each other.<br/>
 * <br/>
 * RapidBeans lets you define a binary association indirectly by defining one or
 * two association end properties each together with a <b>name</b> attribute
 * that of course is simply used as an RapidBean's property name but also has
 * the semantics of a role name.<br/>
 * <br/>
 * <li><b>targettype:</b> the most important attribute of an association is to link only classes of distinct types together. So the targettype definition is mandatory. Simply specify the name of the other class that you want to associate. Either use a fully qualified class name (with packages) or simply the class name if the target class is in the same package.<br/>
 * <br/> <li><b>associationname (currently not implemented):</b> the second important attribute for an association is the association name. The association name usually is a verb an always has the scope of a package i. e. org.rapidbeans.security.hasRole Association names are on one hand useful for documentation reasons in the model but on the other we need them to match two (or later on more) association ends together to one association. There is only one exception where you can omit the
 * association name: a one way navigable association.<br/>
 * <br/> <li><b>inverse:</b> alternatively to an associations name you simply define the name of the target classe's property that implements the related association end. This definition is mandatory at least on one association end.<br/>
 * <br/> <li><b>multiplicity:</b><br/>
 * <br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>- maxmult: </b> defines the minimal amount of association partner instances.<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp ;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Must be a positive integer or -1 which means "unlimited". The default value for this property is -1.<br/>
 * <br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>- minmult: </b> defines the minimal amount of association partner instances.<br/>
 * &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp ;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Must be a positive integer or 0. The default value for this attribute is 0<br/>
 * <br/>
 * </li> <li><b>composition:</b> This maker defines a special form of composition association that associates one single composite instance with one or many component instances. So composition implies multiplicity one at the composite side. Furthermore composition implies a so called "delete cascade". That means if you delete the composite all components will be deleted automatically too. This also holds transitively for components of components.<br/>
 * <br/>
 * <b>Please note:</b> while the role names and the multiplicity are annotated on the opposite classes where you would annotate them in UML, the composition marker is at the composite class side.</b> <br/>
 * <br/>
 * </li>
 * 
 * Besides the very general multiplicity and composition there are further
 * attributes of an association end that in let you define the association more
 * detailed. <li><b>qualified:</b> { true | false }</li> <li><b>flavor:</b> { set | bag }</li> <li><b>ordering:</b> { byId, byPropertyValuesAll, byPropertyValues }</li>
 * 
 * <br/>
 * 
 * <br/>
 * 
 * @author Martin Bluemel
 */
public abstract class PropertyAssociationend extends Property {

	/**
	 * common constructor for Number Properties.
	 * 
	 * @param type
	 *            the Property's type
	 * @param parentBean
	 *            the parent bean
	 */
	public PropertyAssociationend(final TypeProperty type, final RapidBean parentBean) {
		super(type, parentBean);
	}
}
