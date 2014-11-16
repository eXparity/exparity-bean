/*
 * Copyright (c) Modular IT Limited.
 */

package org.exparity.beans;

import static org.exparity.beans.Type.type;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

import org.exparity.beans.TypeInspector;
import org.exparity.beans.TypeVisitor;
import org.exparity.beans.testutils.BeanUtilTestFixture.GetterWithArgs;
import org.exparity.beans.testutils.BeanUtilTestFixture.NameMismatch;
import org.exparity.beans.testutils.BeanUtilTestFixture.OverloadedSetter;
import org.exparity.beans.testutils.BeanUtilTestFixture.Person;
import org.exparity.beans.testutils.BeanUtilTestFixture.SetterWithNoArgs;
import org.exparity.beans.testutils.BeanUtilTestFixture.TypeMismatch;
import org.junit.Test;
import org.mockito.Mockito;

/**
 * @author Stewart Bissett
 */
public class TypeInspectorTest {

	@Test
	public void canInspectAType() {
		TypeVisitor visitor = Mockito.mock(TypeVisitor.class);
		new TypeInspector().inspect(Person.class, visitor);
		verify(visitor).visit(type(Person.class).get("firstname"));
		verify(visitor).visit(type(Person.class).get("surname"));
		verify(visitor).visit(type(Person.class).get("siblings"));
		verifyNoMoreInteractions(visitor);
	}

	@Test
	public void canInspectATypeWithOverloadedSetter() {
		TypeVisitor visitor = Mockito.mock(TypeVisitor.class);
		new TypeInspector().inspect(OverloadedSetter.class, visitor);
		verify(visitor).visit(type(OverloadedSetter.class).propertyNamed("property"));
		verifyNoMoreInteractions(visitor);
	}

	@Test
	public void canSkipInspectATypeWhichHasGetterWithArgs() {
		TypeVisitor visitor = Mockito.mock(TypeVisitor.class);
		new TypeInspector().inspect(GetterWithArgs.class, visitor);
		verifyNoMoreInteractions(visitor);
	}

	@Test
	public void canSkipInspectATypeWhichHasSetterWithNoArgs() {
		TypeVisitor visitor = Mockito.mock(TypeVisitor.class);
		new TypeInspector().inspect(SetterWithNoArgs.class, visitor);
		verifyNoMoreInteractions(visitor);
	}

	@Test
	public void canSkipInspectATypeWhichHasMismatchedTypes() {
		TypeVisitor visitor = Mockito.mock(TypeVisitor.class);
		new TypeInspector().inspect(TypeMismatch.class, visitor);
		verifyNoMoreInteractions(visitor);
	}

	@Test
	public void canInspectATypeWhichHasANameMismatch() {
		TypeVisitor visitor = Mockito.mock(TypeVisitor.class);
		new TypeInspector().inspect(NameMismatch.class, visitor);
		verifyNoMoreInteractions(visitor);
	}

}
