# frozen_string_literal: true
require './dom/base/general_xml_element.rb'

class BuildXml
  BUILD_XML_ROOT = 'build'.freeze
  FILE_NAME = 'build.xml'.freeze
  def initialize(jobPath, number)
    buildDir = "#{jobPath}\\builds\\#{number}"
    @doc = GeneralXmlElement.build_for_doc("#{buildDir}\\#{FILE_NAME}")
  end
  def get_parameter(parameterType, parameterName)
    parameter_element = @doc.get_child_elements("#{BUILD_XML_ROOT}/actions/hudson.model.ParametersAction/parameters")
                            .select {|element| element.get_name == parameterType}
                            .find {|element| element.get_child_element('name').get_text == parameterName}

    return parameter_element.get_child_element('value').get_text
  end
  def get_result
    result_element = @doc.get_child_element("#{BUILD_XML_ROOT}/result")
    return result_element.get_text
  end

end
