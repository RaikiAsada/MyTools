# frozen_string_literal: true
require './dom/base/general_xml_element.rb'

class BuildXml
  BUILD_XML_ROOT = 'build'.freeze
  FILE_NAME = 'build.xml'.freeze
  def initialize(buildsPath, number)
    buildXml_path = File.join(buildsPath, "builds", number.to_s, FILE_NAME)
    @doc = GeneralXmlElement.build_for_doc(buildXml_path)
  end
  def get_parameter(parameterType, parameterName)
    parameter_path = "#{BUILD_XML_ROOT}/actions/hudson.model.ParametersAction/parameters"
    parameter_element = @doc.get_child_elements(parameter_path)
                            .select {|element| element.get_name == parameterType}
                            .find {|element| element.get_child_element('name').get_text == parameterName}

    return parameter_element&.get_child_element('value')&.get_text
  end
  def get_result
    result_element = @doc.get_child_element("#{BUILD_XML_ROOT}/result")
    return result_element.get_text
  end

end
