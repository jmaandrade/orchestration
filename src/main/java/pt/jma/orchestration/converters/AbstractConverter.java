package pt.jma.orchestration.converters;

import java.io.Serializable;

import pt.jma.orchestration.context.config.ConverterType;
import pt.jma.orchestration.util.AbstractConfigurableElement;
import pt.jma.orchestration.util.IConfigurableElement;

public abstract class AbstractConverter extends AbstractConfigurableElement<ConverterType> implements IConfigurableElement<ConverterType> , Serializable  {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3228900509405076007L;

}
