/*
 * Copyright (c) Modular IT Limited.
 */

package uk.co.it.modular.beans;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static uk.co.it.modular.beans.Type.type;
import org.junit.Test;
import org.mockito.Mockito;
import uk.co.it.modular.beans.testutils.BeanUtilTestFixture.GetterWithArgs;
import uk.co.it.modular.beans.testutils.BeanUtilTestFixture.NameMismatch;
import uk.co.it.modular.beans.testutils.BeanUtilTestFixture.OverloadedSetter;
import uk.co.it.modular.beans.testutils.BeanUtilTestFixture.Person;
import uk.co.it.modular.beans.testutils.BeanUtilTestFixture.SetterWithNotArgs;
import uk.co.it.modular.beans.testutils.BeanUtilTestFixture.TypeMismatch;

/**
 * @author <a href="mailto:stewart@modular-it.co.uk">Stewart Bissett</a>
 */
public class TypeInspectorTest {

	@Test
	public void canInspectAType() {
		TypeVisitor visitor = Mockito.mock(TypeVisitor.class);
		Type type = type(Person.class);
		type.visit(visitor);
		verify(visitor).visit(type.propertyNamed("firstname"));
		verify(visitor).visit(type.propertyNamed("surname"));
		verify(visitor).visit(type.propertyNamed("siblings"));
		verifyNoMoreInteractions(visitor);
	}

	@Test
	public void canInspectATypeWithOverloadedSetter() {
		TypeVisitor visitor = Mockito.mock(TypeVisitor.class);
		Type type = type(OverloadedSetter.class);
		type.visit(visitor);
		verify(visitor).visit(type.propertyNamed("property"));
		verifyNoMoreInteractions(visitor);
	}

	@Test
	public void canSkipInspectATypeWhichHasGetterWithArgs() {
		TypeVisitor visitor = Mockito.mock(TypeVisitor.class);
		type(GetterWithArgs.class).visit(visitor);
		verifyNoMoreInteractions(visitor);
	}

	@Test
	public void canSkipInspectATypeWhichHasSetterWithNoArgs() {
		TypeVisitor visitor = Mockito.mock(TypeVisitor.class);
		type(SetterWithNotArgs.class).visit(visitor);
		verifyNoMoreInteractions(visitor);
	}

	@Test
	public void canSkipInspectATypeWhichHasMismatchedTypes() {
		TypeVisitor visitor = Mockito.mock(TypeVisitor.class);
		type(TypeMismatch.class).visit(visitor);
		verifyNoMoreInteractions(visitor);
	}

	@Test
	public void canInspectATypeWhichHasANameMismatch() {
		TypeVisitor visitor = Mockito.mock(TypeVisitor.class);
		type(NameMismatch.class).visit(visitor);
		verifyNoMoreInteractions(visitor);
	}

}
