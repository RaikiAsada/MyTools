# frozen_string_literal: true
require 'rexml/document'

class BuildXml
  BUILD_XML_ROOT = 'build'.freeze
  def initialize(jobPath, number)
    @doc = read_buildXml(jobPath, number)
  end

  def get_parameter(parameterType, parameterName)
    parameter_value = nil
    parameters = get_elements("#{BUILD_XML_ROOT}/actions/hudson.model.ParametersAction/parameters")

    if !parameters
      return parameter_value
    end

    parameters.elements.each(parameterType) do | element |
      if element.elements["name"].text == parameterName
        parameter_value = element.elements["value"].text
      end
    end

    return parameter_value
  end

  def get_result
    elements = get_elements('build/result')

    if !elements
      return nil
    end

    return elements.text
  end

  def get_elements(path)
    if(!@doc)
      return nil
    end

    return @doc.elements[path]
  end

  private def get_build_dir_path(jobPath, number)
    return "#{jobPath}\\builds\\#{number}"
  end
  private def read_buildXml(jobPath, number)
    buildDir = get_build_dir_path(jobPath, number)
    fileName = "build.xml"

    if (!File.exist?("#{buildDir}\\#{fileName}"))
      return nil
    end

    Dir.chdir(buildDir)
    return REXML::Document.new(File.new(fileName))
  end
end
