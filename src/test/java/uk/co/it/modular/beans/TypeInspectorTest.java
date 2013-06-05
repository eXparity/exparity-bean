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
		new TypeInspector().inspect(SetterWithNotArgs.class, visitor);
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
