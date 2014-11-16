/*
 * Copyright (c) Modular IT Limited.
 */

package org.exparity.beans;

import org.exparity.beans.BeanPropertyPath;
import org.junit.Test;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.not;
import static org.junit.Assert.assertThat;

/**
 * Unit test for the {@link PropertyPath} class
 * 
 * @author Stewart Bissett
 */
public class PropertyPathTest {

	@Test
	public void canGetFullPath() {
		assertThat("x.y.x[0].a.b.c", equalTo(new BeanPropertyPath("x.y.x[0].a.b.c").fullPath()));
	}

	@Test
	public void canGetPathWithNotIndexes() {
		assertThat("x.y.x.a.b.c", equalTo(new BeanPropertyPath("x.y.x[0].a.b.c").fullPathWithNoIndexes()));
	}

	@Test
	public void canGetDepth() {
		assertThat(5, equalTo(new BeanPropertyPath("x.y.x[0].a.b.c").depth()));
	}

	@Test
	public void canGetDepthForEmptyString() {
		assertThat(0, equalTo(new BeanPropertyPath("").depth()));
	}

	@Test
	public void canTestForStartsWith() {
		assertThat(true, equalTo(new BeanPropertyPath("x.y.x[0].a.b.c").startsWith("x")));
	}

	@Test
	public void canTestForNotStartingWith() {
		assertThat(false, equalTo(new BeanPropertyPath("x.y.x[0].a.b.c").startsWith("y")));
	}

	@Test
	public void canAppend() {
		assertThat("x.y.x.a", equalTo(new BeanPropertyPath("x.y.x").append("a").fullPath()));
	}

	@Test
	public void canAppendIfEmpty() {
		assertThat("x", equalTo(new BeanPropertyPath("").append("x").fullPath()));
	}

	@Test
	public void canAppendIfNull() {
		assertThat("x", equalTo(new BeanPropertyPath(null).append("x").fullPath()));
	}

	@Test
	public void canAppendIndex() {
		assertThat("x.y.x[0]", equalTo(new BeanPropertyPath("x.y.x").appendIndex(0).fullPath()));
	}

	@Test
	public void canAppendIndexKey() {
		assertThat("x.y.x[key]", equalTo(new BeanPropertyPath("x.y.x").appendIndex("key").fullPath()));
	}

	@Test
	public void canBeEqualIfSamePath() {
		assertThat(new BeanPropertyPath("x.y.z"), equalTo(new BeanPropertyPath("x.y.z")));
	}

	@Test
	public void canBeUnequalIfDifferentPath() {
		assertThat(new BeanPropertyPath("x.y.z"), not(equalTo(new BeanPropertyPath("a.b.c"))));
	}

}
