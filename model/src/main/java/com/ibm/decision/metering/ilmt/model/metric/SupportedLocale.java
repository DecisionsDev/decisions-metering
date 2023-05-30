/*
 *
 *   Copyright IBM Corp. 2022
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 *
 */

package com.ibm.decision.metering.ilmt.model.metric;

import java.util.Locale;

public enum SupportedLocale {
	EN("en"), DE("de"), ES("es"), FR("fr"), IT("it"), JA("ja"), KO("ko"), NL(
			"nl"), PL("pl"), pt_BR("pt", "BR"), RU("ru"), SV("sv"), zh_TW("zh", "TW"), zh(
			"zh");

	private final Locale locale;

	private SupportedLocale(String language) {
		this.locale = new Locale(language);
	}

	private SupportedLocale(String language, String country) {
		this.locale = new Locale(language, country);
	}

	public Locale getLocale() {
		return locale;
	}
}
