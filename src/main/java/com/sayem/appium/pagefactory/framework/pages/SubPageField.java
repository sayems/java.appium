package com.sayem.appium.pagefactory.framework.pages;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Marker interface indicating that a field that extends {@link SubPage}
 * should be loaded when the page is initialized.
 */

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface SubPageField {
}
