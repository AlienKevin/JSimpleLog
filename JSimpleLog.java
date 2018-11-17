package logging;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

public class JSimpleLog {
	private boolean isLogging = true;// all logging default to be ON
	private boolean unspecifiedFormsLog = true;// all unspecified forms' logging default to be ON
	public static String UNSPECIFIED = "unspecified";
	private String form = null;
	private Stack<String> previousForms = new Stack<>();
	private Map<String, Boolean> canFormsLog = new HashMap<>();
	private Map<String, List<String>> categories = new HashMap<>();

	public JSimpleLog() {
		// do nothing
	}

	public void out(String message) {
		if (isLogging) {
			Boolean logFilter = false;// default to off
			if (isCategory(form)) {// current form is CATEGORY
				logFilter = handleCategories();
			} else {// current form is TYPE
				// handle categories of a type
				Boolean canCategoriesLog = checkCategoryLog();
				// type has a high precedence than category in determining logging behavior
				Boolean canTypeLog = checkTypeLog();
				if (canCategoriesLog == null && canTypeLog == null) {
					logFilter = unspecifiedFormsLog; // depends on isLogging and allFormsLog
				} else if (canCategoriesLog == null) {// categories are unspecified
					logFilter = canTypeLog;// only depends on type
				} else if (canTypeLog == null) {// type is unspecified
					logFilter = canCategoriesLog;// only depends on categories
				} else {
					logFilter = canTypeLog;// type dominates categories
				}
			}
			// System.out.println("logFilter: " + logFilter);
			if (logFilter) {
				console(message);
			}
		}
	}

	private boolean handleCategories() {
		Boolean canCategoryLog = canFormsLog.get(form);
		if (canCategoryLog != null) {
			return canCategoryLog;
		}
		return true;
	}

	private Boolean checkTypeLog() {
		if (this.form != null) {// current type is defined
			Boolean canTypeLog = canFormsLog.get(this.form);
			if (canTypeLog != null) {
				return canTypeLog;
			} else {
				return null;// current type's logging behavior is not specified
			}
		} else {// no type is specified
			return null;
		}
	}

	private Boolean checkCategoryLog() {
		List<String> categories = getCategories(this.form);
		if (categories.size() > 0) {// current type belongs to at least one category
			boolean allCategoriesNull = true;
			for (String category : categories) {
				Boolean canLog = canFormsLog.get(category);
				if (canLog != null) {
					allCategoriesNull = false;
					if (canLog == true) {
						return true;
					}
				}
			}
			if (allCategoriesNull) {// current type's categories' logging behaviors
									// are not specified
				return null;
			}
		} else {// current type belongs to no categories
			return null;
		}
		return false;
	}

	public void console(String message) {
		System.out.println(message);
	}

	/**
	 * Turn on logging, some logs may be filtered by their type/categories
	 */
	public void on() {
		isLogging = true;
	}

	/**
	 * Turn off logging, no logs will be produced
	 */
	public void off() {
		isLogging = false;
	}

	/**
	 * Set logging behavior for a specific form (type/category)
	 * 
	 * @param form
	 *            the form to set
	 * @param isLogging
	 *            turn on logging or not
	 */
	public void setFormLog(String form, boolean isLogging) {
		if (form.equalsIgnoreCase(UNSPECIFIED)) {
			unspecifiedFormsLog = isLogging;
		} else {
			canFormsLog.put(standardizeFormInput(form), isLogging);
		}
	}

	public String getForm() {
		return this.form;
	}

	private void setForm(String form) {
		this.form = standardizeFormInput(form);
		this.previousForms.push(this.form);
	}

	public void reset() {
		if (previousForms.size() > 1) {
			this.previousForms.pop();
			this.form = this.previousForms.peek();
		}
	}

	/**
	 * Set the type of the logger from now on
	 * 
	 * @param type
	 *            the type to set to
	 */
	public void setType(String type) {
		setForm(type);
	}

	/**
	 * Set the category of the logger from now on
	 * 
	 * @param category
	 *            the category to set to
	 */
	public void setCategory(String category) {
		setForm(category);
	}

	/**
	 * Categorize one or more types to a category
	 * 
	 * @param category
	 *            the category which contains the types
	 * @param types
	 *            the types to categorize
	 */
	public void categorize(String category, String... types) {
		this.categories.put(category, Arrays.asList(types));
	}

	/**
	 * Add one or more types to an existing category, new types that already exists
	 * in the category are not added. If the category does not exist, create a new
	 * one with the given types
	 * 
	 * @param category
	 *            the category to add to
	 * @param newTypes
	 *            the new types to include in the category
	 */
	public void addTypes(String category, String... newTypes) {
		List<String> oldTypes = this.categories.get(category);
		if (oldTypes == null) {// category does not exist
			categorize(category, newTypes);
		} else {// add to existing category
			for (String type : newTypes) {
				if (!oldTypes.contains(type)) {
					oldTypes.add(type);
				}
			}
		}
	}

	/**
	 * Test if a form is a category or not
	 * 
	 * @param form
	 *            a form to test
	 * @return
	 */
	private boolean isCategory(String form) {
		if (form != null && categories.containsKey(form)) {
			return true;
		}
		return false;
	}

	/**
	 * Get all the categories associated with a given type
	 * 
	 * @param type
	 *            the type to find categories
	 * @return
	 */
	private List<String> getCategories(String type) {
		List<String> categories = new ArrayList<>();
		for (String category : this.categories.keySet()) {
			if (inCategory(type, category)) {
				categories.add(category);
			}
		}
		return categories;
	}

	/**
	 * Test if a type belongs to a category
	 * 
	 * @param type
	 *            the type to test
	 * @param category
	 *            the category to test
	 * @return
	 */
	private boolean inCategory(String type, String category) {
		if (this.categories.get(category).contains(type)) {
			return true;
		}
		return false;
	}

	/**
	 * Standardize form input in String
	 * 
	 * @param form
	 *            String representation of a form
	 * @return
	 */
	private String standardizeFormInput(String form) {
		return form.toLowerCase();
	}
}
